package com.bom.game.entity;

import com.badlogic.gdx.utils.Pool;

public class BombPool {
    
    protected final Pool<Bomb> pool = new Pool<Bomb>() {
	@Override
	protected Bomb newObject() {
		return new Bomb();
	}
    };

    public Pool<Bomb> get() {
        return pool;
    }
}
