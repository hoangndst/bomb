package com.bom.game.entity;

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

}
