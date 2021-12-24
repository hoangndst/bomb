package com.bom.game.entity.Tiles;

import com.badlogic.gdx.math.Rectangle;
import com.bom.game.modules.BitCollision;
import com.bom.game.screen.GameScreen;

public class Wall extends TileBase {
    
    public Wall(GameScreen gameScreen, Rectangle bounds) {
        super(gameScreen, bounds);
        setCollisionFilter(BitCollision.WALL, 
            BitCollision.orOperation(
            BitCollision.BOMBERMAN,
            BitCollision.BOMB,
            BitCollision.FLAME,
            BitCollision.ENEMY
        ));
        this.type = Type.WALL;
    }


    public Rectangle getBounds() {
        return bounds;
    }


    @Override
    public void handleAction() {
        // TODO Auto-generated method stub
    }
}