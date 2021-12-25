package com.bom.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bom.game.ai.AStarMap;
import com.bom.game.ai.AStartPathFinding;
import com.bom.game.entity.Enemy.Balloom;
import com.bom.game.entity.Enemy.EnemyBase;
import com.bom.game.entity.Tiles.Brick;
import com.bom.game.entity.Tiles.TileBase;
import com.bom.game.entity.Tiles.Wall;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.UnitHelper;
import com.bom.game.screen.GameScreen;

public class EntityManager {

	private ArrayList<Wall> walls;
	private ArrayList<Brick> bricks;
	private ArrayList<EntityBase> entities;
	private ArrayList<EnemyBase> enemies;
	private ArrayList<TileBase> items;
	public static ArrayList<ArrayList<Integer>> map = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> initMap = new ArrayList<>();
	private GameScreen gameScreen;
	public static AStarMap aStarMap;

	public EntityManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
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
		aStarMap = new AStarMap(GameManager.mapWidth, GameManager.mapHeight);
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
		// updateMap();
		updateAStarMap();
		// test();
		// for (EnemyBase enemy : enemies) {
		// Bulb bulb = (Bulb) enemy;
		// GameManager.getInstance().pathfinder = new
		// AStartPathFinding(aStarMap);
		// // System.err.println(bulb.body.getPosition().x + " " +
		// bulb.body.getPosition().y);
		// System.err.println(GameManager.getInstance()
		// .pathfinder.findNextNode(bulb.body.getPosition(), new Vector2(8,
		// 8)));
		// }
		// System.out.println(enemies.size());
		// for(int i = 0; i < GameManager.HEIGHT / GameManager.PPM; i++) {
		// for(int j = 0; j < GameManager.WIDTH / GameManager.PPM; j++) {
		// System.err.print(map.get(i).get(j) + " ");
		// }
		// System.err.println();
		// }
		// System.err.println("/");
		ArrayList<EnemyBase> removeEnemies = new ArrayList<>();
		for (EnemyBase enemy : enemies) {
			if (enemy.canDestroy && !enemy.isDead && enemy.timeRemove <= 0) {
				enemy.dead();
				removeEnemies.add(enemy);
			} else {
				enemy.update(delta);
			}

		}
        // System.err.println(removeEnemies.size());
		if (removeEnemies.size() > 0) {
            for (EnemyBase enemy : removeEnemies) {
                enemies.remove(enemy);
            }
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
		return this.enemies.size() <= 0;
	}

	public void updateMap() {
		removeCurrentEntityPosition();
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).enemyLive == 1) {
				Balloom balloom = (Balloom) enemies.get(i);
				int x = UnitHelper
						.snapMetersToGrid(balloom.body.getPosition().x);
				int y = (int) (GameManager.HEIGHT / GameManager.PPM) - 1
						- UnitHelper
								.snapMetersToGrid(balloom.body.getPosition().y);
				System.err.println(x + " " + y);
				if (balloom.canDestroy) {
					map.get(y).set(x, 0);
				} else {
					map.get(y).set(x, 3);
				}
			}
		}
		updateAStarMap();
	}

	public void createMapArray() {
		map.clear();
		for (int i = 0; i < GameManager.HEIGHT / GameManager.PPM; i++) {
			ArrayList<Integer> row = new ArrayList<>();
			map.add(row);
			for (int j = 0; j < GameManager.WIDTH / GameManager.PPM; j++) {
				map.get(i).add(0);
			}
		}
		for (int i = 0; i < this.walls.size(); i++) {
			float x = this.walls.get(i).body.getPosition().x;
			float y = this.walls.get(i).body.getPosition().y;
			float width = (this.walls.get(i).bounds.getWidth()
					/ GameManager.PPM) - 1;
			float height = (this.walls.get(i).bounds.getHeight()
					/ GameManager.PPM) - 1;
			if (height > width) {
				int yEnd = (int) (GameManager.HEIGHT / GameManager.PPM) - 1
						- (int) (y - (height / 2));
				int yStart = (int) (GameManager.HEIGHT / GameManager.PPM) - 1
						- (int) (y + (height / 2));
				for (int j = yStart; j <= yEnd; j++) {
					map.get(j).set((int) (x), 1);
				}
			} else if (width > height) {
				int xStart = (int) (x - (width / 2));
				int xEnd = (int) (x + (width / 2));
				int Y = (int) (GameManager.HEIGHT / GameManager.PPM) - 1
						- (int) (y);
				for (int j = xStart; j <= xEnd; j++) {
					map.get(Y).set(j, 1);
				}
			} else {
				int X = UnitHelper.snapMetersToGrid(
						this.walls.get(i).body.getPosition().x);
				int Y = UnitHelper.snapMetersToGrid(
						this.walls.get(i).body.getPosition().y);
				Y = (int) (GameManager.HEIGHT / GameManager.PPM) - Y - 1;
				map.get(Y).set(X, 1);
			}
		}
		for (int i = 0; i < this.bricks.size(); i++) {
			int x = UnitHelper
					.snapMetersToGrid(this.bricks.get(i).body.getPosition().x);
			int y = UnitHelper
					.snapMetersToGrid(this.bricks.get(i).body.getPosition().y);
			y = (int) (GameManager.HEIGHT / GameManager.PPM) - y - 1;
			map.get(y).set(x, 1);
		}

		// create A* map
		for (int i = 0; i < GameManager.mapHeight; i++) {
			for (int j = 0; j < GameManager.mapWidth; j++) {
				if (map.get(i).get(j) != 0) {
					aStarMap.getNodeAt(j, i).isWall = true;
				}
			}
		}
		GameManager.getInstance().pathfinder = new AStartPathFinding(aStarMap);
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

	private void updateAStarMap() {
		aStarMap = new AStarMap(GameManager.mapWidth, GameManager.mapHeight);
		for (int i = 0; i < GameManager.mapHeight; i++) {
			for (int j = 0; j < GameManager.mapWidth; j++) {
				if (map.get(i).get(j) != 0) {
					aStarMap.getNodeAt(j,
							GameManager.mapHeight - 1 - i).isWall = true;
				}
			}
		}
		ArrayList<Bomb> bombs = gameScreen.bomberman.getBombs();
		for (int i = 0; i < bombs.size(); i++) {
			int x = UnitHelper
					.snapMetersToGrid(bombs.get(i).body.getPosition().x);
			int y = UnitHelper
					.snapMetersToGrid(bombs.get(i).body.getPosition().y);
			aStarMap.getNodeAt(x, y).isWall = true;
		}
		GameManager.getInstance().pathfinder = new AStartPathFinding(aStarMap);
	}

	public void test() {
		for (int i = 0; i < aStarMap.getHeight(); i++) {
			for (int j = 0; j < aStarMap.getWidth(); j++) {
				int y = GameManager.mapHeight - 1 - i;
				System.err.print(aStarMap.getNodeAt(j, y).isWall ? "1 " : "0 ");
			}
			System.err.println();
		}
		System.err.println("/");
	}

}
