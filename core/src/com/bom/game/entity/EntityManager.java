package com.bom.game.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class EntityManager {

    private ArrayList<Wall> walls;
    private ArrayList<Brick> bricks;
    private ArrayList<EntityBase> entityBases;

    public EntityManager() {
        walls = new ArrayList<>();
        bricks = new ArrayList<>();
        entityBases = new ArrayList<>();
    }

    public void addWall(Wall wall) {
        this.walls.add(wall);
    }

    public void addBrick(Brick brick) {
        this.bricks.add(brick);
    }

    public void addEntity(EntityBase entityBase) {
        this.entityBases.add(entityBase);
    }

    public boolean wallContainsPosition(Vector2 position) {
        for (Wall wall : walls) {
            if (wall.getBounds().contains(position.x, position.y)) {
                return true;
            }
        }
        return false;
    }

    public void removeEntity(EntityBase entityBase) {
        this.entityBases.remove(entityBase);
    }

}
