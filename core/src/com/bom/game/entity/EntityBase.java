package com.bom.game.entity;

import com.bom.game.modules.BitCollision;

public abstract class EntityBase {
    
    public EntityType type;
    public boolean canDestroy = false;
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
        this.entityManager = null;
        entityManager.removeEntity(this);
    }

    public enum EntityType {

        BOMBERMAN(BitCollision.BOMBERMAN), BOMB(BitCollision.BOMB), FLAME(BitCollision.FLAME), NULL(
            BitCollision.NULL);

        private final short bitCollision;

        private EntityType(short bitCollision) {
        this.bitCollision = bitCollision;
        }
    }

}
