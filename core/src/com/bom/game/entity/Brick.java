package com.bom.game.entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.Hud;
import com.bom.game.screen.GameScreen;

public class Brick extends TileBase {

    private TiledMap map;

    public Brick(GameScreen gameScreen, Rectangle bounds) {
        super(gameScreen, bounds);
        this.map = gameScreen.getMap();
        setCollisionFilter(BitCollision.BRICK, BitCollision.orOperation(BitCollision.BOMBERMAN,
            BitCollision.BOMB, BitCollision.FLAME, BitCollision.ENEMY));
        this.type = Type.BRICK;

    }

    private TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("background");
        // System.err.println((int) body.getPosition().x + ", " + (int) body.getPosition().y);
        return layer.getCell((int) body.getPosition().x, (int) body.getPosition().y);

    }

    public void handleBom() {
        getCell().setTile(null);
        Filter newFilter = new Filter();
        newFilter.categoryBits = BitCollision.DESTROYED_BRICK;
        newFilter.maskBits = fixture.getFilterData().maskBits;
        fixture.setFilterData(newFilter);
        Hud.addScore(100);
    }

    @Override
    public void handleAction() {
        // TODO Auto-generated method stub

    }
}

