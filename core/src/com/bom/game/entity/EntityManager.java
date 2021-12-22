package com.bom.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.UnitHelper;

public class EntityManager {

	private ArrayList<Wall> walls;
	private ArrayList<Brick> bricks;
	private ArrayList<EntityBase> entities;
	private ArrayList<EnemyBase> enemies;
	private ArrayList<TileBase> items;
    public ArrayList<ArrayList<Integer>> map = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> initMap = new ArrayList<>();

	public EntityManager() {
        for (int i = 0; i < GameManager.HEIGHT / GameManager.PPM; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            map.add(row);
            for (int j = 0; j < GameManager.WIDTH / GameManager.PPM; j++) {
                map.get(i).add(0);
            }
        }

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

	public void addEnemy(EnemyBase enemyBase) {
		this.enemies.add(enemyBase);
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
        updateMap();
        for(int i = 0; i < GameManager.HEIGHT / GameManager.PPM; i++) {
            for(int j = 0; j < GameManager.WIDTH / GameManager.PPM; j++) {
                System.err.print(map.get(i).get(j) + " ");
            }
            System.err.println();
        }
        System.err.println("/");
		EnemyBase temp = null;
		for (EnemyBase enemy : enemies) {
			if (enemy.canDestroy && !enemy.isDead && enemy.timeRemove <= 0) {
				enemy.dead();
				temp = enemy;
			} else {
				enemy.update(delta);
			}

		}
		if (temp != null) {
			this.enemies.remove(temp);
		}
	}

	public void render(SpriteBatch batch) {
		for (EnemyBase enemy : enemies) {
			if (!enemy.isDead) {
				enemy.render(batch);
			}
		}
	}

	public void addItem(TileBase item) {
		this.items.add(item);
	}

	public boolean enemiesIsClear() {
		return enemies.size() <= 0;
	}


    public void updateMap() {
        removeCurrentEntityPosition();
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).enemyLive == 1) {
                Balloom balloom = (Balloom) enemies.get(i);
                int x = UnitHelper.snapMetersToGrid(balloom.body.getPosition().x);
                int y = (int) (GameManager.HEIGHT / GameManager.PPM) - 1 - UnitHelper.snapMetersToGrid(balloom.body.getPosition().y);
                System.err.println(x + " " + y);
                if (balloom.canDestroy) {
                    map.get(y).set(x, 0);
                } else {
                    map.get(y).set(x, 3);
                }
            }
        }
    }

    public void importWall() {
        for (int i = 0; i < this.walls.size(); i++) {
            float x = this.walls.get(i).body.getPosition().x;
            float y = this.walls.get(i).body.getPosition().y;
            float width = (this.walls.get(i).bounds.getWidth() / GameManager.PPM) - 1;
            float height = (this.walls.get(i).bounds.getHeight() / GameManager.PPM) - 1;
            if (height > width) {
                int yEnd = (int) (GameManager.HEIGHT / GameManager.PPM) - 1 - (int) (y - (height / 2));
                int yStart = (int) (GameManager.HEIGHT / GameManager.PPM) - 1 - (int) (y + (height / 2));
                for (int j = yStart; j <= yEnd; j++) {
                    map.get(j).set((int) (x), 1);
                }
            } else if (width > height) {
                int xStart = (int) (x - (width / 2));
                int xEnd = (int) (x + (width / 2));
                int Y = (int) (GameManager.HEIGHT / GameManager.PPM) - 1 - (int) (y);
                for (int j = xStart; j <= xEnd; j++) {
                    map.get(Y).set(j, 1);
                }
            } else {
                int X = UnitHelper.snapMetersToGrid(this.walls.get(i).body.getPosition().x);
                int Y = UnitHelper.snapMetersToGrid(this.walls.get(i).body.getPosition().y);
                Y = (int) (GameManager.HEIGHT / GameManager.PPM) - Y - 1;
                map.get(Y).set(X, 1);
            }
        }
        for (int i = 0; i < this.bricks.size(); i++) {
            int x = UnitHelper.snapMetersToGrid(this.bricks.get(i).body.getPosition().x);
            int y = UnitHelper.snapMetersToGrid(this.bricks.get(i).body.getPosition().y);
            y = (int) (GameManager.HEIGHT / GameManager.PPM) - y - 1;
            map.get(y).set(x, 2);
        }
    }

    public void setInitMap() {
        this.initMap = this.map;
    }

    private void removeCurrentEntityPosition() {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == 3) {
                    map.get(i).set(j, 0);
                }
            }
        }
    }

}
