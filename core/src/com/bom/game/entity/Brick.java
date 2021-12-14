package com.bom.game.entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.bom.game.modules.BitCollision;
import com.bom.game.modules.Paths;
import com.bom.game.modules.TileId;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class Brick extends TileBase {
    
    private TiledMap map;
    
    public Brick(GameScreen gameScreen, Rectangle bounds) {
        super(gameScreen, bounds);
        this.map = gameScreen.getMap();
        setCollisionFilter(BitCollision.BRICK,
            BitCollision.orOperation(
            BitCollision.BOMBERMAN,
            BitCollision.BOMB
        ));

    }

    private TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(Paths.tiledBackgroundLayer);
        return layer.getCell(UnitHelper.snapMetersToGrid(bounds.x), UnitHelper.snapMetersToGrid(bounds.y));
    }

    public void handleBom() {
        getCell().setTile(tileSet.getTile(TileId.DESTROYED_BRICK));
        Filter newFilter = new Filter();
        newFilter.categoryBits = BitCollision.DESTROYED_BRICK;
        newFilter.maskBits = fixture.getFilterData().maskBits;
        
        fixture.setFilterData(newFilter);
    }
}

