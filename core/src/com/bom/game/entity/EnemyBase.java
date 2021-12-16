package com.bom.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class EnemyBase extends EntityBase {

    public float timeMove;
    public float timeRemove;
    public int enemyLive;

    public EnemyBase() {}

    public EnemyBase(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO Auto-generated method stub
    }
    
    public abstract void dead();
}
