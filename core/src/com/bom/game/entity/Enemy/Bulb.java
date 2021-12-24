package com.bom.game.entity.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bom.game.ai.Node;
import com.bom.game.animation.AnimationHandle;
import com.bom.game.entity.Bomb;
import com.bom.game.entity.EntityManager;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.Hud;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class Bulb extends EnemyBase {

	private World world;
	private AnimationHandle animationHandle;
	private float FRAME_TIME = 0.6f;
	private float speed = 2.5f;
	private float bodyDiameter = 0.875f;
	public Body body;
	private static BodyDef bDef = new BodyDef();
	private static FixtureDef fDef = new FixtureDef();
	private String playerPath = "bulb.atlas";
	private Sprite sprite;
	private GameScreen gameScreen;

	public Bulb(GameScreen gameScreen, Ellipse ellipse) {
		super(gameScreen.entityCreator.entityManager);
		this.gameScreen = gameScreen;
		this.timeMove = 0f;
		this.timeRemove = 1f;
		this.enemyLive = 1;
		this.canDestroy = false;
		this.world = gameScreen.getWorld();
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(playerPath));
		animationHandle = new AnimationHandle();
		animationHandle.addAnimation(State.BULB_DEAD.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BULB_DEAD.getValue())));
		animationHandle.addAnimation(State.BULB_IDLE.getValue(),
				new Animation<TextureRegion>(FRAME_TIME,
						atlas.findRegions(State.BULB_IDLE.getValue())));
		animationHandle.setCurrentAnimation(State.BULB_IDLE.getValue());
		sprite = new Sprite(animationHandle.getCurrentFrame());
		sprite.setPosition(ellipse.x, ellipse.y);
		defineBulb(ellipse);
	}

	private void defineBulb(Ellipse ellipse) {
		bDef.type = BodyDef.BodyType.DynamicBody;
		bDef.position.set(UnitHelper.coordPixelsToMeters(ellipse.x, ellipse.y));
		body = world.createBody(bDef);
		CircleShape shape = new CircleShape();
		shape.setRadius(bodyDiameter / 2);
		// shape.setPosition(new Vector2(0, -6 / BomGame.PPM));
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
		Node node = GameManager.getInstance().pathfinder.findNextNode(
				this.body.getPosition(),
				gameScreen.bomberman.body.getPosition());
		System.err.println(node);
		if (node != null) {
			System.err.println("Body: " + this.body.getPosition().x + " "
					+ this.body.getPosition().y);
			float xSource = this.body.getPosition().x;
			float ySource = this.body.getPosition().y;
			float xTarget = node.x + 0.5f;
			float yTarget = node.y + 0.5f;
			if (xSource < xTarget && Math.abs(ySource - yTarget) < 0.2f) {
				body.setLinearVelocity(new Vector2(speed, 0));
			} else if (xSource > xTarget && Math.abs(ySource - yTarget) < 0.2f) {
				body.setLinearVelocity(new Vector2(-speed, 0));
			} else if (ySource < yTarget && Math.abs(xSource - xTarget) < 0.2f) {
				body.setLinearVelocity(new Vector2(0, speed));
			} else if (ySource > yTarget && Math.abs(xSource - xTarget) < 0.2f) {
				body.setLinearVelocity(new Vector2(0, -speed));
			}
		} else {
			Bomb bomb = new Bomb();
			float dis = 1000000f;
			for (Bomb b : gameScreen.bomberman.getBombs()) {	
				if (UnitHelper.distance(this.body.getPosition(), b.body.getPosition()) < dis) {
					bomb = b;
					dis = UnitHelper.distance(this.body.getPosition(), b.body.getPosition());
				}
			}
			if (dis < 2f) {
				float xSource = this.body.getPosition().x;
				float ySource = this.body.getPosition().y;
				float xTarget = bomb.body.getPosition().x;
				float yTarget = bomb.body.getPosition().y;
				int x = MathUtils.floor(xSource);
				int y = MathUtils.floor(ySource);
				System.err.println("Bulb: " + x + " " + y);
				int yMap = GameManager.mapHeight - 1 - y;
				System.err.println(EntityManager.map.get(yMap).get(x + 1) + " " + EntityManager.map.get(yMap).get(x - 1)
						+ " " + EntityManager.map.get(yMap - 1).get(x) + " "
						+ EntityManager.map.get(yMap + 1).get(x));
				if (ySource <= yTarget && Math.abs(xSource - xTarget) < 0.2f) {
					if (EntityManager.map.get(yMap).get(x + 1) == 0 && (0.44 < (ySource - y) && (ySource - y) < 0.56)) {
						body.setLinearVelocity(new Vector2(speed, 0));
					} else if (EntityManager.map.get(yMap).get(x - 1) == 0 && (0.44 < (ySource - y) && (ySource - y) < 0.56)) {
						body.setLinearVelocity(new Vector2(-speed, 0));
					} else if (EntityManager.map.get(yMap + 1).get(x) == 0) {
						body.setLinearVelocity(new Vector2(0, -speed));
					}
				} else if (ySource >= yTarget && Math.abs(xSource - xTarget) < 0.2f) {
					if (EntityManager.map.get(yMap).get(x + 1) == 0 && (0.44 < (ySource - y) && (ySource - y) < 0.56)) {
						body.setLinearVelocity(new Vector2(speed, 0));
					} else if (EntityManager.map.get(yMap).get(x - 1) == 0 && (0.44 < (ySource - y) && (ySource - y) < 0.56)) {
						body.setLinearVelocity(new Vector2(-speed, 0));
					} else if (EntityManager.map.get(yMap - 1).get(x) == 0) {
						body.setLinearVelocity(new Vector2(0, speed));
					}
				} else if (xSource <= xTarget && Math.abs(ySource - yTarget) < 0.2f) {
					if (EntityManager.map.get(yMap + 1).get(x) == 0 && (0.44 < (xSource - x) && (xSource - x) < 0.56)) {
						body.setLinearVelocity(new Vector2(0, -speed));
					} else if (EntityManager.map.get(yMap - 1).get(x) == 0 && (0.44 < (xSource - x) && (xSource - x) < 0.56)) {
						body.setLinearVelocity(new Vector2(0, speed));
					} else if (EntityManager.map.get(yMap).get(x - 1) == 0) {
						body.setLinearVelocity(new Vector2(-speed, 0));
					}
				} else if (xSource >= xTarget && Math.abs(ySource - yTarget) < 0.2f) {
					if (EntityManager.map.get(yMap + 1).get(x) == 0 && (0.44 < (xSource - x) && (xSource - x) < 0.56)) {
						body.setLinearVelocity(new Vector2(0, -speed));
					} else if (EntityManager.map.get(yMap - 1).get(x) == 0 && (0.44 < (xSource - x) && (xSource - x) < 0.56)) {
						body.setLinearVelocity(new Vector2(0, speed));
					} else if (EntityManager.map.get(yMap).get(x + 1) == 0) {
						body.setLinearVelocity(new Vector2(speed, 0));
					}
				}
			} else {
				timeMove -= delta;
				if (timeMove <= 0) {
					int random = getRandomNumber(1, 5);
					switch (random) {
						case 1 :
							body.setLinearVelocity(new Vector2(speed, 0));
							break;
						case 2 :
							body.setLinearVelocity(new Vector2(-speed, 0));
							break;
						case 3 :
							body.setLinearVelocity(new Vector2(0, speed));
							break;
						case 4 :
							body.setLinearVelocity(new Vector2(0, -speed));
							break;
					}
					timeMove = 3f;
				}
			}
		}
	}

	@Override
	public void dead() {
		if (timeRemove <= 0) {
			this.world.destroyBody(this.body);
			Hud.addScore(1500);
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
			animationHandle.setCurrentAnimation(State.BULB_DEAD.getValue());
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

		BULB_IDLE("bulb_idle"), BULB_DEAD("bulb_dead");

		private String value;

		private State(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
