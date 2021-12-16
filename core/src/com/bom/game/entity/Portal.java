package com.bom.game.entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.BitCollision;
import com.bom.game.screen.GameScreen;

public class Portal extends TileBase {

    private TiledMap map;

    public Portal(GameScreen gameScreen, Rectangle bounds) {
        super(gameScreen, bounds);
        this.map = gameScreen.getMap();
        setCollisionFilter(BitCollision.ITEM,
            BitCollision.orOperation(
            BitCollision.BOMBERMAN
        ));
        this.type = Type.PORTAL;
        this.body.getFixtureList().get(0).setSensor(true);
    }

    private TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("item");
        // System.err.println((int) body.getPosition().x + ", " +  (int) body.getPosition().y);
        return layer.getCell((int) body.getPosition().x, (int) body.getPosition().y);

    }

    public void handleAction() {
        // getCell().setTile(null);
        if (GameManager.bombermanHasKey) {
            GameManager.bombermanInPortal = true;
            Filter newFilter = new Filter();
            newFilter.categoryBits = BitCollision.DESTROYED_BRICK;
            newFilter.maskBits = fixture.getFilterData().maskBits;
            fixture.setFilterData(newFilter);
        }
    }
    
}