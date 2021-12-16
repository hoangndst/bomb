package com.bom.game.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bom.game.screen.GameScreen;
import com.bom.game.modules.Paths;
import com.bom.game.modules.UnitHelper;

public abstract class TileBase {
    
    protected Body body;
    protected Fixture fixture;
    protected Rectangle bounds;
    public static TiledMapTileSet tileSet;
    public Type type = Type.NULL;
    protected float timeRemove = 1;

    public TileBase(GameScreen screen, Rectangle bounds) {
        this.bounds = bounds;
        
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(
                UnitHelper.coordPixelsToMeters(
                    UnitHelper.screenToBox2D(bounds.getX(), bounds.getWidth()),
                    UnitHelper.screenToBox2D(bounds.getY(), bounds.getHeight())
                )
        );
        body = screen.getWorld().createBody(bdef);

        shape.setAsBox(
                UnitHelper.pixelsToMeters(bounds.getWidth() / 2),
                UnitHelper.pixelsToMeters(bounds.getHeight() / 2)
        );
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        tileSet = screen.getMap().getTileSets().getTileSet(Paths.tileset_All);
    }
    
    protected void setCollisionFilter(short categoryBit, short maskBits) {
        Filter f = new Filter();
        f.categoryBits = categoryBit;
        f.maskBits = maskBits;
        fixture.setFilterData(f);
    }

    public abstract void handleAction();

    public static enum Type {
        BRICK,
        WALL,
        SPEED_UP,
        FLAME_UP,
        BOMB_UP,
        NULL
    }
}