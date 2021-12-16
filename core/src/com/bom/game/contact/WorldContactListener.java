package com.bom.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bom.game.entity.Balloom;
import com.bom.game.entity.Bomb;
import com.bom.game.entity.Bomberman;
import com.bom.game.entity.Brick;
import com.bom.game.entity.EntityBase;
import com.bom.game.entity.TileBase;
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
        GameManager.getInstance().playSound("Die.ogg");
        EntityBase entity = (EntityBase) fixA.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.canDestroy = true;
      } else if (fixB.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        GameManager.getInstance().playSound("Die.ogg");
        EntityBase entity = (EntityBase) fixB.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.canDestroy = true;
      } else if (fixB.getFilterData().categoryBits == BitCollision.BRICK) {
        // TileBase tile = (TileBase) fixB.getUserData();
        Brick brick = (Brick) fixB.getUserData();
        brick.handleBom();
      } else if (fixA.getFilterData().categoryBits == BitCollision.BRICK) {
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
      } else if (fixA.getFilterData().categoryBits == BitCollision.ENEMY) {
        GameManager.getInstance().playSound("EnemyDie.ogg");
        Balloom entity = (Balloom) fixA.getUserData();
        entity.canDestroy = true;
      } else if (fixB.getFilterData().categoryBits == BitCollision.ENEMY) {
        GameManager.getInstance().playSound("EnemyDie.ogg");
        Balloom entity = (Balloom) fixB.getUserData();
        entity.canDestroy = true;
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
      } else if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        GameManager.getInstance().playSound("Die.ogg");
        EntityBase entity = (EntityBase) fixA.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.canDestroy = true;
      } else if (fixB.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        GameManager.getInstance().playSound("Die.ogg");
        EntityBase entity = (EntityBase) fixB.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.canDestroy = true;
      }
    }
    if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN
        || fixB.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
      if (fixA.getFilterData().categoryBits == BitCollision.ITEM
          || fixB.getFilterData().categoryBits == BitCollision.ITEM) {
        GameManager.getInstance().playSound("Powerup.ogg");
        Fixture itemFixture = fixA.getFilterData().categoryBits == BitCollision.ITEM ? fixA : fixB;
        Fixture bombermanFixture =
            fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN ? fixA : fixB;
        TileBase tile = (TileBase) itemFixture.getUserData();
        Bomberman bomberman = (Bomberman) bombermanFixture.getUserData();
        tile.handleAction();
        TileBase.Type type = tile.type;
        System.err.println(type);
        switch (type) {
          case SPEED_UP:
            System.err.println("speed up");
            bomberman.speedUp();
            break;
          case FLAME_UP:
            System.err.println("FLAME_UP");
            bomberman.FlameUp();
            break;
          case BOMB_UP:
            System.err.println("BOMB_UP");
            bomberman.BombUp();
            break;
          default:
            break;
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
      Fixture bombFix = fixA.getFilterData().categoryBits == BitCollision.BOMB ? fixA : fixB;
      Bomb bomb = (Bomb) bombFix.getUserData();
      bomb.sensorFlag = false;
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
    // TODO Auto-generated method stub

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
    // TODO Auto-generated method stub

  }

}
