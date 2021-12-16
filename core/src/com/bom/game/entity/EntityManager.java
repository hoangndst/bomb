package com.bom.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class EntityManager {

    private ArrayList<Wall> walls;
    private ArrayList<Brick> bricks;
    private ArrayList<EntityBase> entities;
    private ArrayList<EntityBase> enemies;
    private ArrayList<TileBase> items;

    public EntityManager() {
        walls = new ArrayList<>();
        bricks = new ArrayList<>();
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void addWall(Wall wall) {
        this.walls.add(wall);
    }

    public void addBrick(Brick brick) {
        this.bricks.add(brick);
    }

    public void addEntity(EntityBase entityBase) {
        this.entities.add(entityBase);
    }

    public void addEnemy(EntityBase entityBase) {
        this.enemies.add(entityBase);
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
        this.enemies.remove(entityBase);
    }

    public void update(float delta) {
        // for (TileBase item : items) {
        //     System.err.println(item);
        // }
        EntityBase temp = null;
        for (EntityBase entityBase : enemies) {
            if (entityBase instanceof Balloom) {
                Balloom balloom = (Balloom) entityBase;
                if (balloom.canDestroy && !balloom.isDead && balloom.timeRemove <= 0) {
                    balloom.dead();
                    temp = balloom;
                } else {
                    balloom.update(delta);
                }
            }
        }
        if (temp != null) {
            this.enemies.remove(temp);
        }
    }

    public void render(SpriteBatch batch) {
        for (EntityBase entityBase : enemies) {
            if (entityBase instanceof Balloom && !entityBase.isDead) {
                entityBase.render(batch);
            } 
        }
    }

    public void addItem(TileBase item) {
        this.items.add(item);
    }

}
