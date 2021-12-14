package com.bom.game.entity;

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
            BitCollision.FLAME
        ));
    }

    public Rectangle getBounds() {
        return bounds;
    }
}