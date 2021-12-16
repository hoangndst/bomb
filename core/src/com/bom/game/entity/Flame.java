package com.bom.game.entity;

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
import com.bom.game.animation.AnimationHandle;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.UnitHelper;

public class Flame extends EntityBase implements Disposable {

    public World world;
    private AnimationHandle animationHandle;
    private float FRAME_TIME = 0.6f;
    public Body body;
    private static BodyDef bDef = new BodyDef();
    private static FixtureDef fDef = new FixtureDef();
    private String playerPath = "flame.atlas";
    private Sprite sprite;
    private float bodyDiameter = 0.875f;
    private Bomb bomb;

    public Flame(Bomb bomb, Vector2 position, State direction) {
        super(bomb.getEntityManager());
        this.bomb = bomb;
        this.world = bomb.world;
        this.type = EntityType.FLAME;
        // this.gameScreen = bomb.gameScreen;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
        animationHandle = new AnimationHandle();
        animationHandle.addAnimation(State.FLAME_DOWN.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.FLAME_DOWN.getValue())));
        animationHandle.addAnimation(State.FLAME_UP.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.FLAME_UP.getValue())));
        animationHandle.addAnimation(State.FLAME_LEFT.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.FLAME_LEFT.getValue())));
        animationHandle.addAnimation(State.FLAME_RIGHT.getValue(),
            new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.FLAME_RIGHT.getValue())));
        animationHandle.setCurrentAnimation(direction.getValue());
        sprite = new Sprite(animationHandle.getCurrentFrame());
        sprite.setPosition(position.x, position.y);
        defineFlame(position);
    }

    private void defineFlame(Vector2 position) {
        bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(UnitHelper.coordScreenToBox2D(position.x, position.y, bodyDiameter / 2));
        bDef.fixedRotation = true;
        body = world.createBody(bDef);

        CircleShape cShape = new CircleShape();
        cShape.setRadius(bodyDiameter / 2);
        cShape.setPosition(new Vector2(0.05f, 0.05f));
        fDef = new FixtureDef();
        fDef.shape = cShape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = BitCollision.FLAME;
        fDef.filter.maskBits = BitCollision.orOperation(BitCollision.BOMBERMAN, BitCollision.WALL,
            BitCollision.BRICK, BitCollision.BOMB, BitCollision.ENEMY);

        body.createFixture(fDef).setUserData(this);
    }

    public void update(float delta) {
        sprite.setBounds(UnitHelper.box2DToScreen(body.getPosition().x, bodyDiameter),
            UnitHelper.box2DToScreen(body.getPosition().y, bodyDiameter), UnitHelper.pixelsToMeters(16),
            UnitHelper.pixelsToMeters(16));
        sprite.setRegion(animationHandle.getCurrentFrame());
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void removeFromWorld() {
        world.destroyBody(body);
    }

    public static enum State {
        FLAME_UP("flame_y"), FLAME_DOWN("flame_y"), FLAME_LEFT("flame_x"), FLAME_RIGHT("flame_x");
        String stateName;

        private State(String stateName) {
            this.stateName = stateName;
        }

        public String getValue() {
            return stateName;
        }

        public static Vector2 getOffSet(State state) {
            switch (state) {
                case FLAME_UP:
                return new Vector2(0, 1);
                case FLAME_DOWN:
                return new Vector2(0, -1);
                case FLAME_LEFT:
                return new Vector2(-1, 0);
                case FLAME_RIGHT:
                return new Vector2(1, 0);
                default:
                return new Vector2(0, 0);
            }
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

}
