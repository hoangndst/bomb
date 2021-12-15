package com.bom.game.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bom.game.BomGame;
import com.bom.game.contact.WorldContactListener;
import com.bom.game.entity.Bomb;
import com.bom.game.entity.BombPool;
import com.bom.game.entity.Bomberman;
import com.bom.game.entity.EntityCreator;
import com.bom.game.entity.Flame;
import com.bom.game.entity.EntityBase;

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
    private WorldContactListener worldContactListener;

    public GameScreen(BomGame bomGame) {
        this.bomGame = bomGame;
        world = new World(new Vector2(0, 0), true);
        camera = new OrthographicCamera();
        viewport = new FitViewport(BomGame.WIDTH / BomGame.PPM, BomGame.HEIGHT / BomGame.PPM, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        map = new TmxMapLoader().load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / BomGame.PPM);
        entityCreator = new EntityCreator(this);
        entityCreator.createEntity();
        bombPool = new BombPool();
        bomberman = new Bomberman(this, new Vector2(8, 8));
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();
    }

    private void update(float delta) {
        world.step(1 / 60f, 6, 2);
        renderer.setView(camera);
        bomberman.update(delta);
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
        // bomberman.getBombs()
        if (entities.size() > 0) {
            bomberman.getBombs().removeAll(entities);
        }
        entities.clear();
        // if (bomberman.canDestroy) {
        //     bomberman.dead();
        // }
        entities.clear();
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
        update(delta);
        renderer.render();
        b2dr.render(world, camera.combined);
        // bomberman.time -= delta;
        remove(delta);
        bomGame.batch.setProjectionMatrix(camera.combined);
        bomGame.batch.begin();
        bomberman.render(bomGame.batch);
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

    public BombPool getBombPool() {
        return this.bombPool;
    }
}
