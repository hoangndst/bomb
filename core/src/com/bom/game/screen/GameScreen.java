package com.bom.game.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bom.game.BomGame;
import com.bom.game.contact.WorldContactListener;
import com.bom.game.entity.Bomb;
import com.bom.game.entity.BombPool;
import com.bom.game.entity.Bomberman;
import com.bom.game.entity.EntityBase;
import com.bom.game.entity.EntityCreator;
import com.bom.game.entity.Flame;
import com.bom.game.manager.GameManager;
import com.bom.game.modules.Hud;

public class GameScreen implements Screen {

	public World world;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera camera;
	private Viewport viewport;
	private BomGame bomGame;
	public final EntityCreator entityCreator;
	private Bomberman bomberman;
	private BombPool bombPool;
	private ArrayList<EntityBase> entities = new ArrayList<EntityBase>();
	private boolean paused = false;
	private boolean showB2DDebugRenderer = false;
	private Hud hud;
	private Skin skin;
	private Stage stage;
	private Window pauseWindow;
	private TextButton audiButton;

	public GameScreen(BomGame bomGame) {
		this.bomGame = bomGame;
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera();
		viewport = new FitViewport(GameManager.WIDTH / GameManager.PPM,
				GameManager.HEIGHT / GameManager.PPM, camera);
		camera.position.set(viewport.getWorldWidth() / 2,
				viewport.getWorldHeight() / 2, 0);
		map = new TmxMapLoader().load("map1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / GameManager.PPM);
		entityCreator = new EntityCreator(this);
		entityCreator.createEntity();
		bombPool = new BombPool();
		bomberman = new Bomberman(this, new Vector2(8, 8));
		world.setContactListener(new WorldContactListener());
		b2dr = new Box2DDebugRenderer();
		skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
		audiButton = new TextButton(
				GameManager.audioEnabled ? "Audio: ON" : "Audio: OFF", skin);
		hud = new Hud(bomGame.getSpriteBatch());
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		renderer.setView(camera);
		bomberman.update(delta);
		entityCreator.entityManager.update(delta);
		if (GameManager.bombermanLive <= 0 || hud.isTimeUp()) {
			GameManager.getInstance().reset();
			bomGame.setScreen(new GameOverScreen(bomGame));
		}
		hud.update(delta);
		if (GameManager.bombermanHasKey && GameManager.bombermanInPortal
				&& entityCreator.entityManager.enemiesIsClear()) {

			bomGame.setScreen(new VictoryScreen(bomGame));
		}
	}

	private void remove(float delta) {
		for (Bomb bomb : bomberman.getBombs()) {
			if (bomb.canDestroy) {
				if (bomb.timeRemove <= 0) {
					for (Flame flame : bomb.getFlames()) {
						flame.removeFromWorld();
					}
					entities.add(bomb);
					bombPool.get().free(bomb);
				}
			}
		}
		if (entities.size() > 0) {
			bomberman.getBombs().removeAll(entities);
		}
		entities.clear();
	}

	public TiledMap getMap() {
		return map;
	}

	public World getWorld() {
		return world;
	}

	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			showB2DDebugRenderer = !showB2DDebugRenderer;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			paused = !paused;
			if (paused) {
				GameManager.getInstance().playSound("Pause.ogg");
				GameManager.getInstance().pauseMusic();
			} else {
				GameManager.getInstance().playSound("Pause.ogg");
				GameManager.getInstance().playMusic("SuperBomberman-Area1.ogg",
						true);
			}
		}
	}

	@Override
	public void show() {
		GameManager.getInstance().playMusic("SuperBomberman-Area1.ogg", true);
		stage = new Stage(
				new FitViewport(GameManager.WIDTH, GameManager.HEIGHT),
				bomGame.batch);
		pauseWindow = new Window("Pause", skin);
		pauseWindow.setPosition(
				(GameManager.WIDTH - pauseWindow.getWidth()) / 2 + 10,
				(GameManager.HEIGHT - pauseWindow.getHeight()) / 2 + 20);
		pauseWindow.setVisible(paused);
		pauseWindow.setSize(GameManager.WIDTH / 2, GameManager.HEIGHT / 2);
		TextButton continueButton = new TextButton("Continue", skin);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				paused = false;
				GameManager.getInstance().playMusic("SuperBomberman-Area1.ogg",
						true);
			}
		});

		TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				bomGame.setScreen(new MenuScreen(bomGame));
			}
		});

		audiButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (GameManager.audioEnabled) {
					GameManager.audioEnabled = false;
					audiButton.setText("Audio: OFF");
				} else {
					GameManager.audioEnabled = true;
					audiButton.setText("Audio: ON");
				}
			}
		});
		pauseWindow.add(continueButton);
		pauseWindow.row();
		pauseWindow.add(audiButton);
		pauseWindow.row();
		pauseWindow.add(exitButton);

		stage.addActor(pauseWindow);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		if (!paused) {
			update(delta);
		}
		// update(delta);
		renderer.render();
		if (showB2DDebugRenderer) {
			b2dr.render(world, camera.combined);
		}
		// bomberman.time -= delta;
		remove(delta);
		bomGame.batch.setProjectionMatrix(camera.combined);
		bomGame.batch.begin();
		bomberman.render(bomGame.batch);
		entityCreator.entityManager.render(bomGame.batch);
		bomGame.batch.end();
		pauseWindow.setVisible(paused);
		bomGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		hud.dispose();
	}

	public BombPool getBombPool() {
		return this.bombPool;
	}

	public Hud getHud() {
		return hud;
	}
}
