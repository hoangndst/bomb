package com.bom.game.entity;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.bom.game.animation.AnimationHandle;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class Bomberman extends EntityBase implements Disposable {

    private World world;
    private ArrayList<Bomb> bombs;
    private int bombCount = 1;
    private int flameLength = 1;
    private AnimationHandle animationHandle;
    private float FRAME_TIME = 0.6f;
    private float speed = 2.5f;
    public Body body;
    private static BodyDef bDef = new BodyDef();
    private static FixtureDef fDef = new FixtureDef();
    private String playerPath = "bomberman.atlas";
    private Sprite sprite;
    private GameScreen gameScreen;
    private State direction = State.IDLE_DOWN;
    private float bombCooldown = 1, bombCooldownTimer = bombCooldown;
    private boolean canPlaceBombs = true;
    private BombPool bombPool;
    public float time = 1.5f;
    private Vector2 initPosition;

    public Bomberman(GameScreen gameScreen, Vector2 position) {
        super(gameScreen.entityCreator.entityManager);
        this.bombPool = gameScreen.getBombPool();
        canDestroy = false;
        this.world = gameScreen.getWorld();
        this.bombs = new ArrayList<Bomb>();
        this.gameScreen = gameScreen;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
        animationHandle = new AnimationHandle();
        animationHandle.addAnimation(State.WALK_DOWN.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.WALK_DOWN.getValue())));
        animationHandle.addAnimation(State.WALK_LEFT.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.WALK_LEFT.getValue())));
        animationHandle.addAnimation(State.WALK_RIGHT.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.WALK_RIGHT.getValue())));
        animationHandle.addAnimation(State.WALK_UP.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.WALK_UP.getValue())));
        animationHandle.addAnimation(State.IDLE_DOWN.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.IDLE_DOWN.getValue())));
        animationHandle.addAnimation(State.IDLE_LEFT.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.IDLE_LEFT.getValue())));
        animationHandle.addAnimation(State.IDLE_RIGHT.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.IDLE_RIGHT.getValue())));
        animationHandle.addAnimation(State.IDLE_UP.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.IDLE_UP.getValue())));
        animationHandle.addAnimation(State.DEAD.getValue(),
            new Animation<TextureRegion>(FRAME_TIME + 1, atlas.findRegions(State.DEAD.getValue())));
        animationHandle.setCurrentAnimation(State.IDLE_DOWN.getValue());
        sprite = new Sprite(animationHandle.getCurrentFrame());
        sprite.setPosition(position.x, position.y);
        definePlayer(position);
        this.initPosition = position;
    }

    public void handleInput(float delta) {
        if (!canDestroy) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                animationHandle.setCurrentAnimation(State.WALK_UP.getValue());
                this.body.setLinearVelocity(new Vector2(0, speed));
                direction = State.IDLE_UP;
            // this.body.applyLinearImpulse(new Vector2(0, 0.05f), this.body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                animationHandle.setCurrentAnimation(State.WALK_DOWN.getValue());
                this.body.setLinearVelocity(new Vector2(0, -speed));
                direction = State.IDLE_DOWN;
            // this.body.applyLinearImpulse(new Vector2(0, -0.05f), this.body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                animationHandle.setCurrentAnimation(State.WALK_LEFT.getValue());
                this.body.setLinearVelocity(new Vector2(-speed, 0));
                direction = State.IDLE_LEFT;
            // this.body.applyLinearImpulse(new Vector2(-0.05f, 0), this.body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                animationHandle.setCurrentAnimation(State.WALK_RIGHT.getValue());
                this.body.setLinearVelocity(new Vector2(speed, 0));
                direction = State.IDLE_RIGHT;
            // this.body.applyLinearImpulse(new Vector2(0.05f, 0), this.body.getWorldCenter(), true);
            } else {
                animationHandle.setCurrentAnimation(direction.getValue());
                this.body.setLinearVelocity(0, 0);
            }
        } else {
            this.body.setLinearVelocity(0, 0);
        }
    }

    private void checkExplode(float deltaTime) {
        if (!canPlaceBombs) {
            bombCooldownTimer -= deltaTime;
            if (bombCooldownTimer < 0 && !canDestroy) 
                canPlaceBombs = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canPlaceBombs) {
            if (this.bombCount > 0 && canPlaceBombs) {
                GameManager.getInstance().playSound("PlaceBomb.ogg");
                Bomb bomb = bombPool.get().obtain();
                bomb = new Bomb();
                bomb.init(this, UnitHelper.coordBox2DSnapToGrid(body.getPosition()), flameLength);
                bombs.add(bomb);
                bombCount--;
                canPlaceBombs = false;
                bombCooldownTimer = bombCooldown;
            }
        }
    }

    public void speedUp() {
        speed += 1;
    }

    public void BombUp() {
        bombCount++;
    }

    public void FlameUp() {
        flameLength++;
    }

    public void update(float deltaTime) {
        GameManager.timeGhostMode -= deltaTime;
        handleInput(deltaTime);
        checkExplode(deltaTime);
        sprite.setBounds(UnitHelper.box2DToScreen(body.getPosition().x, 0.875f),
            UnitHelper.box2DToScreen(body.getPosition().y, 0.875f),
            UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionWidth()),
            UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionHeight()));
        sprite.setPosition(UnitHelper.box2DToScreen(body.getPosition().x, 0.875f),
            UnitHelper.box2DToScreen(body.getPosition().y, 0.875f));
        sprite.setRegion(animationHandle.getCurrentFrame());
        for (Bomb bomb : bombs) {
            bomb.update(deltaTime);
        }
        if (canDestroy) {
            time -= deltaTime;
            animationHandle.setCurrentAnimation(State.DEAD.getValue());
            dead();
        }
    }

    public void render(SpriteBatch batch) {
        for (Bomb bomb : bombs) {
            bomb.render(batch);
        }
        sprite.draw(batch);
    }

    public void dead() {
        canPlaceBombs = false;
        if (time <= 0 && GameManager.bombermanLive > 0) {
            GameManager.bombermanLive--;
            this.bombCount = 1;
            this.flameLength = 1;
            this.speed = 2.5f;
            this.world.destroyBody(this.body);
            definePlayer(initPosition);
            this.direction = State.IDLE_DOWN;
            this.canDestroy = false;
            this.time = 1.5f;
            GameManager.timeGhostMode = 3f;
        }
    }

    public void definePlayer(Vector2 position) {
        
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(UnitHelper.coordScreenToBox2D(position.x, position.y, 0.875f / 2));

        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.875f / 2);
        fDef.filter.categoryBits = BitCollision.BOMBERMAN;
        fDef.filter.maskBits = BitCollision.orOperation(BitCollision.WALL, BitCollision.BRICK,
            BitCollision.BOMB, BitCollision.FLAME, BitCollision.ENEMY, BitCollision.ITEM);
        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        for (Bomb bomb : bombs) {
            bomb.dispose();
        }
    }

    private enum State {

        IDLE_UP("idle_up"), 
        IDLE_DOWN("idle_down"), 
        IDLE_LEFT("idle_left"), 
        IDLE_RIGHT("idle_right"), 
        WALK_LEFT("walk_left"), 
        WALK_RIGHT("walk_right"), 
        WALK_UP("walk_up"), 
        WALK_DOWN("walk_down"), 
        DEAD("dead");

        private String value;

        private State(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public World getWorld() {
        return this.world;
    }

    public void recoverBomb() {
        bombCount++;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public int getFlameLength() {
        return flameLength;
    }

}
