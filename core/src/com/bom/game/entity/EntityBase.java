package com.bom.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class EntityBase {
    
    public boolean canDestroy = false;
    public boolean isDead = false;
    public EntityManager entityManager;

    public EntityBase() {}

    public EntityBase(EntityManager entityManager) {
        this.entityManager = entityManager;
        addToEntityManager(entityManager);
    }

    public void addToEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        entityManager.addEntity(this);
    }

    public void removeFromEntityManager() {
        entityManager.removeEntity(this);
        this.entityManager = null;
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);

}
