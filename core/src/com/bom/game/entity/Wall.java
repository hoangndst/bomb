package com.bom.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bom.game.modules.BitCollision;
import com.bom.game.screen.GameScreen;
import com.bom.game.utils.UnitHelper;

public class Wall {

  protected Body body;
  protected Fixture fixture;
  protected Rectangle bounds;

  public Wall(GameScreen screen, Rectangle bounds) {
    this.bounds = bounds;
    BodyDef bdef = new BodyDef();
    FixtureDef fdef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    bdef.type = BodyDef.BodyType.StaticBody;
    bdef.position.set(
        UnitHelper.coordPixelsToMeters(UnitHelper.screenToBox2D(bounds.getX(), bounds.getWidth()),
            UnitHelper.screenToBox2D(bounds.getY(), bounds.getHeight())));
    body = screen.getB2World().createBody(bdef);

    shape.setAsBox(UnitHelper.pixelsToMeters(bounds.getWidth() / 2),
        UnitHelper.pixelsToMeters(bounds.getHeight() / 2));
    fdef.shape = shape;
    fixture = body.createFixture(fdef);
    Filter f = new Filter();
    f.categoryBits = BitCollision.WALL;
    f.maskBits =
        BitCollision.orOperation(BitCollision.BOMBERMAN, BitCollision.BOMB, BitCollision.FLAME);
    fixture.setFilterData(f);
  }

  protected void setCollisionFilter(short categoryBit, short maskBits) {
    Filter f = new Filter();
    f.categoryBits = categoryBit;
    f.maskBits = maskBits;
    fixture.setFilterData(f);
  }
}