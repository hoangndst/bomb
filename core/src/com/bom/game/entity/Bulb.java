package com.bom.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bom.game.animation.AnimationHandle;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class Bulb extends EntityBase {
    
    private World world;
    private AnimationHandle animationHandle;
    private float FRAME_TIME = 0.6f;
    private float speed = 2.5f;
    public Body body;
    private static BodyDef bDef = new BodyDef();
    private static FixtureDef fDef = new FixtureDef();
    private static Circle circle = new Circle();
    private String playerPath = "bulb.atlas";
    private Sprite sprite;
    private GameScreen gameScreen;
    public float timeMove = 0f;
    public float timeRemove = 1f;

    public Bulb(GameScreen gameScreen, Ellipse ellipse) {
        super(gameScreen.entityCreator.entityManager);
        canDestroy = false;
        this.world = gameScreen.getWorld();
        this.type = EntityType.BOMBERMAN;
        this.gameScreen = gameScreen;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
        animationHandle = new AnimationHandle();
        animationHandle.addAnimation(State.BULB_DEAD.getValue(), new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BULB_DEAD.getValue())));
        animationHandle.addAnimation(State.BULB_IDLE.getValue(), new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BULB_IDLE.getValue())));
        animationHandle.setCurrentAnimation(State.BULB_IDLE.getValue());
        sprite = new Sprite(animationHandle.getCurrentFrame());
        sprite.setPosition(ellipse.x, ellipse.y);
        defineBalloom(ellipse);
    }

    private void defineBalloom(Ellipse ellipse) {
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(UnitHelper.coordPixelsToMeters(ellipse.x, ellipse.y));
        body = world.createBody(bDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(0.875f / 2);
        // shape.setPosition(new Vector2(0, -6 / BomGame.PPM));
        fDef.filter.categoryBits = BitCollision.ENEMY;
        fDef.filter.maskBits = BitCollision.orOperation(BitCollision.WALL, BitCollision.BRICK,
            BitCollision.BOMB, BitCollision.FLAME, BitCollision.BOMBERMAN, BitCollision.ENEMY);
        fDef.shape = shape;
        // fdef.isSensor = true;
        body.createFixture(fDef).setUserData(this);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void randomMove(float delta) {
        timeMove -= delta;
        if (timeMove <= 0) {
            int random = getRandomNumber(1, 5);
            switch (random) {
                case 1:
                    body.setLinearVelocity(new Vector2(speed, 0));
                    break;
                case 2:
                    body.setLinearVelocity(new Vector2(-speed, 0));
                    break;
                case 3:
                    body.setLinearVelocity(new Vector2(0, speed));
                    break;
                case 4:
                    body.setLinearVelocity(new Vector2(0, -speed));
                    break;
            }
            timeMove = 3f;
        }
    }


    public void dead() {
        if (timeRemove <= 0) {
            this.world.destroyBody(this.body);
            isDead = true;
        }
    }

    public void update(float delta) {
        if (canDestroy) {
            this.body.setLinearVelocity(new Vector2(0, 0));
            animationHandle.setCurrentAnimation(State.BULB_DEAD.getValue());
            timeRemove -= delta;
        } else  {
            randomMove(delta);
        }
        sprite.setBounds(UnitHelper.box2DToScreen(body.getPosition().x, 0.875f),
            UnitHelper.box2DToScreen(body.getPosition().y, 0.875f),
            UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionWidth()),
            UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionHeight()));
        sprite.setPosition(UnitHelper.box2DToScreen(body.getPosition().x, 0.875f),
            UnitHelper.box2DToScreen(body.getPosition().y, 0.875f));
        sprite.setRegion(animationHandle.getCurrentFrame());
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private enum State {

        BULB_IDLE("bulb_idle"),
        BULB_DEAD("bulb_dead");

        private String value;

        private State(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
