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
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class Bomb extends EntityBase implements Poolable, Disposable {
    
    public World world;
    private int flameLength = 3;
    private ArrayList<Flame> flames;
    private AnimationHandle animationHandle;
    private float FRAME_TIME = 0.6f;
    private float speed = 2.5f;
    public Body body;
    private static BodyDef bDef = new BodyDef();
    private static FixtureDef fDef = new FixtureDef();
    private String playerPath = "bomb.atlas";
    private Sprite sprite;
    private GameScreen gameScreen;
    private float countDown = 3;
    private float bodyDiameter = 0.875f;
    private boolean sensorFlag = true;
    // private State direction = State.IDLE_DOWN;

    public Bomb() {
        canDestroy = false;
        flames = new ArrayList<Flame>();
        this.world = gameScreen.getWorld();
        this.type = EntityType.BOMB;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
        animationHandle = new AnimationHandle();
        animationHandle.addAnimation(State.BOMB_IDLE.getValue(), new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BOMB_IDLE.getValue())));
        animationHandle.addAnimation(State.BOMB_EXPLODE.getValue(), new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BOMB_EXPLODE.getValue())));
        animationHandle.setCurrentAnimation(State.BOMB_IDLE.getValue());
        sprite = new Sprite(animationHandle.getCurrentFrame());
    }

    public Bomb(GameScreen gameScreen, Vector2 position) {
        super(gameScreen.entityCreator.entityManager);
        canDestroy = false;
        flames = new ArrayList<Flame>();
        this.world = gameScreen.getWorld();
        this.type = EntityType.BOMB;
        this.gameScreen = gameScreen;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
        animationHandle = new AnimationHandle();
        animationHandle.addAnimation(State.BOMB_IDLE.getValue(), new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BOMB_IDLE.getValue())));
        animationHandle.addAnimation(State.BOMB_EXPLODE.getValue(), new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions(State.BOMB_EXPLODE.getValue())));
        animationHandle.setCurrentAnimation(State.BOMB_IDLE.getValue());
        sprite = new Sprite(animationHandle.getCurrentFrame());
        sprite.setPosition(position.x, position.y);
        defineBomb(position);
    }

    private void defineBomb(Vector2 position) {
        bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(
                UnitHelper.coordScreenToBox2D(position.x, position.y, bodyDiameter / 2)
        );
        bDef.fixedRotation = true;
        body = world.createBody(bDef);
        CircleShape cShape = new CircleShape();
        cShape.setRadius(bodyDiameter / 2);
        fDef = new FixtureDef();
        fDef.shape = cShape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = BitCollision.BOMB;
        fDef.filter.maskBits = BitCollision.orOperation(
                BitCollision.BOMBERMAN,
                BitCollision.WALL,
                BitCollision.BRICK,
                BitCollision.BOMB,
                BitCollision.FLAME
        );
        
        body.createFixture(fDef).setUserData("bomb");
    }

    private void willExplode(float delta) {
        countDown -= delta;
        if (countDown <= 0 && !canDestroy) {
            explode();
        }
    }

    private void explode() {
        animationHandle.setCurrentAnimation(State.BOMB_EXPLODE.getValue());
        for (Flame.State direction : Flame.State.values()) {
            Vector2 position, nextPosition;
            for (int i = 0; i <= flameLength; i++) {
                
                position = UnitHelper.coordBox2DSnapToGrid(
                        body.getPosition().add(
                                Flame.State.getOffSet(direction).scl(i)
                        )
                );
                
                Vector2 temp = body.getPosition().add(
                        Flame.State.getOffSet(direction).scl(i + 1)
                );
                
                nextPosition = UnitHelper.coordMetersToPixels(temp.x, temp.y);
                
                if (i != 0) {
                    //System.out.printf("(%.2f  %.2f)\n",position.x, position.y);
                    //number++;
                    Flame flame = new Flame(this, position, direction);
                    this.flames.add(flame);
                }
                
                if (entityManager.wallContainsPosition(nextPosition))
                    break;
            }
        }
        canDestroy = true;
    }

    public void update(float delta) {
        willExplode(delta);
        if (!sensorFlag) {
            if (this.body != null) {
                this.body.getFixtureList().get(0).setSensor(false);
            }
        }
        sprite.setBounds(
                UnitHelper.box2DToScreen(this.body.getPosition().x,
                        this.bodyDiameter),
                UnitHelper.box2DToScreen(this.body.getPosition().y,
                        this.bodyDiameter),
                UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionWidth()),
                UnitHelper.pixelsToMeters(animationHandle.getCurrentFrame().getRegionHeight()));
        sprite.setRegion(animationHandle.getCurrentFrame());
        for (Flame flame : flames) {
            flame.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        for (Flame flame : flames) {
            flame.render(batch);
        }
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    private enum State {
        BOMB_IDLE("bomb_idle"),
        BOMB_EXPLODE("bomb_explode");
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
    }

    @Override
    public void dispose() {
        this.sprite.getTexture().dispose();
        for (Flame flame : flames) {
            flame.dispose();
        }
    }
}
