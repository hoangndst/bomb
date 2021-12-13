package com.bom.game.entity;


public enum EntityType {

    BOMBERMAN(CollisionBits.PLAYER),
    BOMB(CollisionBits.BOMB),
    FIRE(CollisionBits.FIRE),
    NULL(CollisionBits.NULL);

    private final short collisionBit;

    private EntityType(short collisionBit) {
        this.collisionBit = collisionBit;
    }
}

public abstract class EntityBase {
    public EntityType type;
    public boolean canDestroy = false;

}
