package com.bom.game.entity;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.bom.game.animation.AnimationHandle;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.UnitHelper;

public class Bomb extends EntityBase implements Poolable, Disposable {

    public World world;
    private int flameLength;
    private ArrayList<Flame> flames;
    private AnimationHandle animationHandle;
    private float FRAME_TIME = 0.6f;
    public Body body;
    private static BodyDef bDef = new BodyDef();
    private static FixtureDef fDef = new FixtureDef();
    private static CircleShape cShape = new CircleShape();
    private String playerPath = "bomb.atlas";
    private Sprite sprite;
    public float countDown = 2f;
    private float bodyDiameter = 0.9f;
    public boolean sensorFlag = true;
    private Bomberman bombOwner;
    public float timeRemove;
    public boolean canMove = true;
    public boolean canKick = false;
    // private State direction = State.IDLE_DOWN;

    public Bomb() {
        timeRemove = 0.8f;
        canDestroy = false;
        flames = new ArrayList<Flame>();
        this.type = EntityType.BOMB;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
        animationHandle = new AnimationHandle();
        animationHandle.addAnimation(State.BOMB_IDLE.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BOMB_IDLE.getValue())));
        animationHandle.addAnimation(State.BOMB_EXPLODE.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BOMB_EXPLODE.getValue())));
        animationHandle.setCurrentAnimation(State.BOMB_IDLE.getValue());
        sprite = new Sprite(animationHandle.getCurrentFrame());
    }

    public void init(Bomberman bombOwner, Vector2 position, int flameLength) {
        this.bombOwner = bombOwner;
        this.world = bombOwner.getWorld();
        this.flameLength = flameLength;
        addToEntityManager(bombOwner.entityManager);
        sprite.setPosition(position.x, position.y);
        defineBomb(position);
    }

    private void defineBomb(Vector2 position) {
        bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(UnitHelper.coordScreenToBox2D(position.x, position.y, bodyDiameter / 2));
        bDef.fixedRotation = true;
        body = world.createBody(bDef);
        cShape = new CircleShape();
        cShape.setRadius(0.9f / 2);
        fDef = new FixtureDef();
        fDef.shape = cShape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = BitCollision.BOMB;
        fDef.filter.maskBits = BitCollision.orOperation(BitCollision.BOMBERMAN, BitCollision.WALL,
            BitCollision.BRICK, BitCollision.FLAME, BitCollision.ENEMY, BitCollision.BOMB);

        body.createFixture(fDef).setUserData(this);
    }

    private void willExplode(float delta) {
        countDown -= delta;
        if (countDown <= 0 && !canDestroy) {
        this.body.setLinearVelocity(new Vector2(0, 0));
        System.err.println(body.getPosition().x + " " + body.getPosition().y);
        // bDef.position.set(UnitHelper.coordScreenToBox2D((int) body.getPosition().x, (int)
        // body.getPosition().y, bodyDiameter / 2));
        sprite.setPosition(UnitHelper.coordBox2DSnapToGrid(body.getPosition()).x,
            UnitHelper.coordBox2DSnapToGrid(body.getPosition()).y);
        explode();
        }
    }

    private void explode() {
        GameManager.getInstance().playSound("Explosion.ogg");
        animationHandle.setCurrentAnimation(State.BOMB_EXPLODE.getValue());
        Flame flameMid =
            new Flame(this, UnitHelper.coordBox2DSnapToGrid(body.getPosition()), Flame.State.FLAME_UP);
        for (Flame.State direction : Flame.State.values()) {
        Vector2 position, nextPosition;
        for (int i = 0; i <= flameLength; i++) {

            position = UnitHelper
                .coordBox2DSnapToGrid(body.getPosition().add(Flame.State.getOffSet(direction).scl(i)));

            Vector2 temp = body.getPosition().add(Flame.State.getOffSet(direction).scl(i + 1));

            nextPosition = UnitHelper.coordMetersToPixels(temp.x, temp.y);

            if (i != 0) {
            // System.out.printf("(%.2f %.2f)\n",position.x, position.y);
            // number++;
            Flame flame = new Flame(this, position, direction);
            this.flames.add(flame);
            }

            if (entityManager.wallContainsPosition(nextPosition))
            break;
        }
        }
        this.flames.add(flameMid);
        canDestroy = true;
    }

    @Override
    public void update(float delta) {
        if (countDown <= 0) {
        timeRemove -= delta;
        }
        willExplode(delta);
        if (!sensorFlag) {
        if (this.body != null) {
            this.body.getFixtureList().get(0).setSensor(false);
        }
        }
        if (!canMove) {
        this.body.setType(BodyDef.BodyType.StaticBody);
        }
        if (countDown > 0) {
        sprite.setBounds(UnitHelper.box2DToScreen(this.body.getPosition().x, this.bodyDiameter),
            UnitHelper.box2DToScreen(this.body.getPosition().y, this.bodyDiameter),
            UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionWidth()),
            UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionHeight()));
        }
        sprite.setRegion(animationHandle.getCurrentFrame());
        for (Flame flame : flames) {
        flame.update(delta);
        }
        // System.err.println(this.body.getPosition().x + " " + this.body.getPosition().y);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        for (int i = 0; i < flames.size() - 1; i++) {
        flames.get(i).render(batch);
        }
    }

    private enum State {
        BOMB_IDLE("bomb_idle"), BOMB_EXPLODE("bomb_explode");

        String stateName;

        private State(String stateName) {
        this.stateName = stateName;
        }

        public String getValue() {
        return stateName;
        }
    }

    @Override
    public void reset() {
        this.sensorFlag = true;
        this.countDown = 3;
        world.destroyBody(body);
        animationHandle.setCurrentAnimation(null);
        sprite = new Sprite();
        removeFromEntityManager();
        bombOwner.recoverBomb();
        bombOwner = null;
        world = null;
        flameLength = 0;
        flames.clear();
    }

    @Override
    public void dispose() {
        this.sprite.getTexture().dispose();
        for (Flame flame : flames) {
        flame.dispose();
        }
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public ArrayList<Flame> getFlames() {
        return this.flames;
    }
}
