package com.bom.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bom.game.entity.Bomb;
import com.bom.game.entity.Bomberman;
import com.bom.game.entity.Brick;
import com.bom.game.entity.EntityBase;
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
        bomberman.canDestroy = true;
      } else if (fixB.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
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
      }
    }
    if (fixA.getFilterData().categoryBits == BitCollision.BOMB
        || fixB.getFilterData().categoryBits == BitCollision.BOMB) {
      if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        EntityBase entity = (EntityBase) fixA.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.duplicateBombPlace = true;
      } else if (fixB.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        EntityBase entity = (EntityBase) fixB.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.duplicateBombPlace = true;
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
      if (fixA.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        EntityBase entity = (EntityBase) fixA.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.duplicateBombPlace = false;
      } else if (fixB.getFilterData().categoryBits == BitCollision.BOMBERMAN) {
        EntityBase entity = (EntityBase) fixB.getUserData();
        Bomberman bomberman = (Bomberman) entity;
        bomberman.duplicateBombPlace = false;
      }
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
