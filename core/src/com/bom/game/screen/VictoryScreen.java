package com.bom.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bom.game.BomGame;
import com.bom.game.manager.GameManager;

public class VictoryScreen extends ScreenAdapter {

    private final BomGame game;
    private final SpriteBatch batch;

    private FitViewport viewport;
    private Stage stage;

    private BitmapFont font;

    public VictoryScreen(BomGame game) {
        this.game = game;
        batch = game.getSpriteBatch();
    }

    @Override
    public void show() {
        viewport = new FitViewport(640, 480);
        stage = new Stage(viewport, batch);

        font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label gameOverLabel = new Label("Victory", labelStyle);
        gameOverLabel.setPosition((640 - gameOverLabel.getWidth()) / 2, 226f);

        GameManager.getInstance().playMusic("Victory.ogg", false);

        stage.addActor(gameOverLabel);

        stage.addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.fadeOut(2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game));
                    }
                })));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
