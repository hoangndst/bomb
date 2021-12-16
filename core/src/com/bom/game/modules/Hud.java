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
import com.bom.game.BomGame;

public class Hud implements Disposable {

  // Scene2D.ui Stage and its own Viewport for HUD
  public Stage stage;
  private Viewport viewport;

  // Mario score/time Tracking Variables
  private Integer worldTimer;
  private boolean timeUp; // true when the world timer reaches 0
  private float timeCount;
  private static Integer score;
  private BitmapFont font;
  // Scene2D widgets
  private Label countdownLabel;
  private static Label scoreLabel;
  private Label timeLabel;
  private Label levelLabel;
  private Label textLabel;
  private Label bomberLabel;

  public Hud(SpriteBatch sb) {
    font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));
    Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
    // define our tracking variables
    worldTimer = 10;
    timeCount = 0;
    score = 0;

    // setup the HUD viewport using a new camera seperate from our gamecam
    // define our stage using that viewport and our games spritebatch
    viewport = new FitViewport(BomGame.WIDTH, BomGame.HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, sb);

    // define a table used to organize our hud's labels
    Table table = new Table();
    // Top-Align table
    table.top();
    // make the table fill the entire stage
    table.setFillParent(true);

    // define our labels using the String, and a Label style consisting of a font and color
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
    // add our labels to our table, padding the top, and giving them all equal width with expandX
    table.add(bomberLabel).expandX().padTop(10);
    table.add(textLabel).expandX().padTop(10);
    table.add(timeLabel).expandX().padTop(10);
    // add a second row to our table
    table.row();
    table.add(scoreLabel).expandX();
    table.add(levelLabel).expandX();
    table.add(countdownLabel).expandX();

    // add our table to the stage
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
