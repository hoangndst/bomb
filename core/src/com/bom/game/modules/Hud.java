package com.bom.game.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bom.game.manager.GameManager;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private boolean timeUp;
    private float timeCount;
    public static int score;
    private static int live;
    private BitmapFont font;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label textLabel;
    private Label bomberLabel;
    private Label bombermanLives;
    private static Label liveLabel;

    public Hud(SpriteBatch sb) {
        font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        worldTimer = 180;
        timeCount = 0;
        score = 0;
        live = GameManager.bombermanLive;
        viewport = new FitViewport(GameManager.WIDTH, GameManager.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        liveLabel = new Label(String.format("%02d", live), labelStyle);
        liveLabel.setFontScale(0.2f);
        countdownLabel = new Label(String.format("%03d", worldTimer), labelStyle);
        countdownLabel.setFontScale(0.2f);
        scoreLabel = new Label(String.format("%06d", score), labelStyle);
        scoreLabel.setFontScale(0.2f);
        timeLabel = new Label("Time", labelStyle);
        timeLabel.setFontScale(0.2f);
        levelLabel = new Label("1-1", labelStyle);
        levelLabel.setFontScale(0.2f);
        textLabel = new Label("Bomberman Map", labelStyle);
        textLabel.setFontScale(0.2f);
        bomberLabel = new Label("Score", labelStyle);
        bomberLabel.setFontScale(0.2f);
        bombermanLives = new Label("Lives", labelStyle);
        bombermanLives.setFontScale(0.2f);

        table.add(bomberLabel).expandX().padTop(10);
        table.add(textLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(bombermanLives).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(liveLabel).expandX();
        stage.addActor(table);

    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
        live = GameManager.bombermanLive;
        liveLabel.setText(String.format("%02d", live));
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}
