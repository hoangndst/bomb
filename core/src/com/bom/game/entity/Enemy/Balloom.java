package com.bom.game.entity.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bom.game.animation.AnimationHandle;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.Hud;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class Balloom extends EnemyBase {

	private World world;
	private AnimationHandle animationHandle;
	private float FRAME_TIME = 0.6f;
	private float speed = 2.5f;
	public Body body;
	private float bodyDiameter = 0.875f;
	private static BodyDef bDef = new BodyDef();
	private static FixtureDef fDef = new FixtureDef();
	private String playerPath = "balloom.atlas";
	private Sprite sprite;

	public Balloom(GameScreen gameScreen, Ellipse ellipse) {
		super(gameScreen.entityCreator.entityManager);
		this.timeMove = 0f;
		this.timeRemove = 1f;
		this.enemyLive = 1;
		this.canDestroy = false;
		this.world = gameScreen.getWorld();
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
		animationHandle = new AnimationHandle();
		animationHandle.addAnimation(State.BALLOOM_UP.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BALLOOM_UP.getValue())));
		animationHandle.addAnimation(State.BALLOOM_DEAD.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BALLOOM_DEAD.getValue())));
		animationHandle.addAnimation(State.BALLOOM_DOWN.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BALLOOM_DOWN.getValue())));
		animationHandle.addAnimation(State.BALLOOM_RIGHT.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BALLOOM_RIGHT.getValue())));
		animationHandle.addAnimation(State.BALLOOM_LEFT.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BALLOOM_LEFT.getValue())));
		animationHandle.setCurrentAnimation(State.BALLOOM_DOWN.getValue());
		sprite = new Sprite(animationHandle.getCurrentFrame());
		sprite.setPosition(ellipse.x, ellipse.y);
		defineBalloom(ellipse);
	}

	private void defineBalloom(Ellipse ellipse) {
		bDef.type = BodyDef.BodyType.DynamicBody;
		bDef.position.set(UnitHelper.coordPixelsToMeters(ellipse.x, ellipse.y));
		body = world.createBody(bDef);
		CircleShape shape = new CircleShape();
		shape.setRadius(bodyDiameter / 2);
		fDef.filter.categoryBits = BitCollision.ENEMY;
		fDef.filter.maskBits = BitCollision.orOperation(BitCollision.WALL,
				BitCollision.BRICK, BitCollision.BOMB, BitCollision.FLAME,
				BitCollision.BOMBERMAN);
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
				case 1 :
					animationHandle.setCurrentAnimation(
							State.BALLOOM_RIGHT.getValue());
					body.setLinearVelocity(new Vector2(speed, 0));
					break;
				case 2 :
					animationHandle
							.setCurrentAnimation(State.BALLOOM_LEFT.getValue());
					body.setLinearVelocity(new Vector2(-speed, 0));
					break;
				case 3 :
					animationHandle
							.setCurrentAnimation(State.BALLOOM_UP.getValue());
					body.setLinearVelocity(new Vector2(0, speed));
					break;
				case 4 :
					animationHandle
							.setCurrentAnimation(State.BALLOOM_DOWN.getValue());
					body.setLinearVelocity(new Vector2(0, -speed));
					break;
			}
			timeMove = 3f;
		}
	}

	@Override
	public void dead() {
		if (timeRemove <= 0) {
			Hud.addScore(1000);
			this.world.destroyBody(this.body);
			isDead = true;
		}
	}

	@Override
	public void update(float delta) {
		if (enemyLive <= 0) {
			canDestroy = true;
		}
		if (canDestroy) {
			this.body.setLinearVelocity(new Vector2(0, 0));
			animationHandle.setCurrentAnimation(State.BALLOOM_DEAD.getValue());
			timeRemove -= delta;
		} else {
			randomMove(delta);
		}
		sprite.setBounds(
				UnitHelper.box2DToScreen(body.getPosition().x, bodyDiameter),
				UnitHelper.box2DToScreen(body.getPosition().y, bodyDiameter),
				UnitHelper.pixelsToMeters(
						animationHandle.getCurrentFrame().getRegionWidth()),
				UnitHelper.pixelsToMeters(
						animationHandle.getCurrentFrame().getRegionHeight()));
		sprite.setPosition(
				UnitHelper.box2DToScreen(body.getPosition().x, bodyDiameter),
				UnitHelper.box2DToScreen(body.getPosition().y, bodyDiameter));
		sprite.setRegion(animationHandle.getCurrentFrame());
	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	private enum State {

		BALLOOM_DOWN("balloom_down"), BALLOOM_UP("balloom_up"), BALLOOM_LEFT(
				"balloom_left"), BALLOOM_RIGHT(
						"balloom_right"), BALLOOM_DEAD("balloom_dead");

		private String value;

		private State(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}