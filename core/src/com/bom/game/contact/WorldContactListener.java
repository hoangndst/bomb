package com.bom.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bom.game.entity.Bomb;
import com.bom.game.entity.Bomberman;
import com.bom.game.entity.EntityBase;
import com.bom.game.entity.Enemy.EnemyBase;
import com.bom.game.entity.Tiles.Brick;
import com.bom.game.entity.Tiles.TileBase;
import com.bom.game.entity.Tiles.TileBase.Type;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.BitCollision;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		if (fixA.getFilterData().categoryBits == BitCollision.FLAME
				|| fixB.getFilterData().categoryBits == BitCollision.FLAME) {
			if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
				EntityBase entity = (EntityBase) fixA.getUserData();
				Bomberman bomberman = (Bomberman) entity;
				if (!bomberman.canDestroy) {
					if (GameManager.timeGhostMode <= 0
							&& !bomberman.canDestroy) {
						GameManager.getInstance().playSound("Die.ogg");
						bomberman.canDestroy = true;
					}
				}
			} else if (fixB
					.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
				EntityBase entity = (EntityBase) fixB.getUserData();
				Bomberman bomberman = (Bomberman) entity;
				if (!bomberman.canDestroy) {
					if (GameManager.timeGhostMode <= 0
							&& !bomberman.canDestroy) {
						GameManager.getInstance().playSound("Die.ogg");
						bomberman.canDestroy = true;
					}
				}
			} else if (fixB
					.getFilterData().categoryBits == BitCollision.BRICK) {
				// TileBase tile = (TileBase) fixB.getUserData();
				Brick brick = (Brick) fixB.getUserData();
				brick.handleBom();
			} else if (fixA
					.getFilterData().categoryBits == BitCollision.BRICK) {
				// TileBase tile = (TileBase) fixA.getUserData();
				// Brick brick = (Brick) tile;
				Brick brick = (Brick) fixA.getUserData();
				brick.handleBom();
			} else if (fixA.getFilterData().categoryBits == BitCollision.BOMB) {
				Bomb bomb = (Bomb) fixA.getUserData();
				bomb.countDown = 0;
			} else if (fixB.getFilterData().categoryBits == BitCollision.BOMB) {
				Bomb bomb = (Bomb) fixB.getUserData();
				bomb.countDown = 0;
			} else if (fixA
					.getFilterData().categoryBits == BitCollision.ENEMY) {
				GameManager.getInstance().playSound("EnemyDie.ogg");
				EnemyBase enemy = (EnemyBase) fixA.getUserData();
				enemy.enemyLive -= 1;
			} else if (fixB
					.getFilterData().categoryBits == BitCollision.ENEMY) {
				GameManager.getInstance().playSound("EnemyDie.ogg");
				EnemyBase enemy = (EnemyBase) fixB.getUserData();
				enemy.enemyLive -= 1;
			}
		}
		if (fixA.getFilterData().categoryBits == BitCollision.ENEMY
				|| fixB.getFilterData().categoryBits == BitCollision.ENEMY) {
			if (fixA.getFilterData().categoryBits == BitCollision.BOMB) {
				Bomb bomb = (Bomb) fixA.getUserData();
				bomb.canMove = false;
			} else if (fixB.getFilterData().categoryBits == BitCollision.BOMB) {
				Bomb bomb = (Bomb) fixB.getUserData();
				bomb.canMove = false;
			} else if (fixA
					.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
				EntityBase entity = (EntityBase) fixA.getUserData();
				Bomberman bomberman = (Bomberman) entity;
				if (GameManager.timeGhostMode <= 0 && !bomberman.canDestroy) {
					bomberman.canDestroy = true;
					GameManager.getInstance().playSound("Die.ogg");
				}
			} else if (fixB
					.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
				EntityBase entity = (EntityBase) fixB.getUserData();
				Bomberman bomberman = (Bomberman) entity;
				if (GameManager.timeGhostMode <= 0 && !bomberman.canDestroy) {
					bomberman.canDestroy = true;
					GameManager.getInstance().playSound("Die.ogg");
				}
			}
		}
		if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN || fixB
				.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
			if (fixA.getFilterData().categoryBits == BitCollision.ITEM
					|| fixB.getFilterData().categoryBits == BitCollision.ITEM) {
				Fixture itemFixture = fixA
						.getFilterData().categoryBits == BitCollision.ITEM
								? fixA
								: fixB;
				Fixture bombermanFixture = fixA
						.getFilterData().categoryBits == BitCollision.BOMBERMAN
								? fixA
								: fixB;
				TileBase tile = (TileBase) itemFixture.getUserData();
				Bomberman bomberman = (Bomberman) bombermanFixture
						.getUserData();
				tile.handleAction();
				TileBase.Type type = tile.type;
				if (type != Type.PORTAL) {
					GameManager.getInstance().playSound("Powerup.ogg");
				}
				System.err.println(type);
				switch (type) {
					case SPEED_UP :
						System.err.println("speed up");
						bomberman.speedUp();
						break;
					case FLAME_UP :
						System.err.println("FLAME_UP");
						bomberman.FlameUp();
						break;
					case BOMB_UP :
						System.err.println("BOMB_UP");
						bomberman.BombUp();
						break;
					default :
						break;
				}
			} else if (fixA.getFilterData().categoryBits == BitCollision.BOMB
					|| fixB.getFilterData().categoryBits == BitCollision.BOMB) {
				Fixture bombFixture = fixA
						.getFilterData().categoryBits == BitCollision.BOMB
								? fixA
								: fixB;
				Bomb bomb = (Bomb) bombFixture.getUserData();
				if (bomb.canKick) {
					GameManager.getInstance().playSound("KickBomb.ogg");
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		if (fixA.getFilterData().categoryBits == BitCollision.BOMB
				|| fixB.getFilterData().categoryBits == BitCollision.BOMB) {
			Fixture bombFix = fixA
					.getFilterData().categoryBits == BitCollision.BOMB
							? fixA
							: fixB;
			Bomb bomb = (Bomb) bombFix.getUserData();
			bomb.sensorFlag = false;
			bomb.canKick = true;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		if (fixA.getFilterData().categoryBits == BitCollision.ENEMY
				|| fixB.getFilterData().categoryBits == BitCollision.ENEMY) {
			if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
				EntityBase entity = (EntityBase) fixA.getUserData();
				Bomberman bomberman = (Bomberman) entity;
				if (GameManager.timeGhostMode <= 0 && !bomberman.canDestroy) {
					bomberman.canDestroy = true;
					GameManager.getInstance().playSound("Die.ogg");
				}
			} else if (fixB
					.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
				EntityBase entity = (EntityBase) fixB.getUserData();
				Bomberman bomberman = (Bomberman) entity;
				if (GameManager.timeGhostMode <= 0 && !bomberman.canDestroy) {
					bomberman.canDestroy = true;
					GameManager.getInstance().playSound("Die.ogg");
				}
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
