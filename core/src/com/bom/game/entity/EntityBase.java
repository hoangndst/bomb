package com.bom.game.entity;

import com.bom.game.modules.Bit;

public abstract class EntityBase {
    public EntityType type;
    public boolean canDestroy = false;
    public EntityManager entityManager;

    public EntityBase(EntityManager entityManager) {
        this.entityManager = entityManager;
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

        BOMBERMAN(Bit.PLAYER),
        BOMB(Bit.BOMB),
        FIRE(Bit.FIRE),
        NULL(Bit.NULL);
    
        private final short collisionBit;
    
        private EntityType(short collisionBit) {
            this.collisionBit = collisionBit;
        }
    }

}
