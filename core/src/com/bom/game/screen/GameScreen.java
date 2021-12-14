package com.bom.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bom.game.BomGame;
import com.bom.game.entity.EntityCreator;

public class GameScreen implements Screen {

    private World world;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BomGame bomGame;
    public final EntityCreator entityCreator;

    public GameScreen(BomGame bomGame) {
        this.bomGame = bomGame;
        world = new World(new Vector2(0, 0), true);
        camera = new OrthographicCamera();
        viewport = new FitViewport(BomGame.WIDTH / BomGame.PPM, BomGame.HEIGHT / BomGame.PPM, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        map = new TmxMapLoader().load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / BomGame.PPM);
        entityCreator = new EntityCreator(this);
        entityCreator.createEntity();
    }

    private void update() {
        world.step(1 / 60f, 6, 2);
        renderer.setView(camera);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update();
        renderer.render();
        bomGame.batch.setProjectionMatrix(camera.combined);
        bomGame.batch.begin();
        bomGame.batch.end();
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

    }
}
