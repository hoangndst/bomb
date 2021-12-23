package com.bom.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bom.game.BomGame;
import com.bom.game.manager.GameManager;

public class MenuScreen implements Screen {

	private BomGame game;
	private SpriteBatch batch;
	private FitViewport viewport;
	private Stage stage;

	private BitmapFont font;

	private Texture backgroundTexture;
	Label musicLabel;
	private Image indicator0;
	private Image indicator1;
	private float positionX;
	private float positionY;
	private int currentSelection;
	private boolean selected;

	public MenuScreen(BomGame game) {
		GameManager.score = 0;
		GameManager.getInstance().reset();
		this.game = game;
		this.batch = game.getSpriteBatch();
	}

	@Override
	public void show() {
		viewport = new FitViewport(640, 490);
		stage = new Stage(viewport, batch);
		font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
		Label titleLabel = new Label("Bomberman", labelStyle);
		titleLabel.setFontScale(1.5f);
		titleLabel.setPosition(160, 340);
		Label newGameLabel = new Label("New Game", labelStyle);
		newGameLabel.setPosition((640 - newGameLabel.getWidth()) / 2, 240);
		musicLabel = new Label(
				GameManager.audioEnabled ? "Music: ON" : "Music: OFF",
				labelStyle);
		musicLabel.setPosition((640 - newGameLabel.getWidth()) / 2, 180);
		Label ExitLabel = new Label("Exit", labelStyle);
		ExitLabel.setPosition((640 - ExitLabel.getWidth()) / 2, 120);
		Pixmap pixmap = new Pixmap(640, 490, Pixmap.Format.RGB888);
		pixmap.setColor(102 / 255.0f, 153 / 255.0f, 0 / 255.0f, 0);
		pixmap.fill();
		backgroundTexture = new Texture(pixmap);
		pixmap.dispose();
		Image background = new Image(backgroundTexture);

		positionX = 120;
		positionY = 240;

		TextureAtlas textureAtlas = GameManager.getInstance().getAssetManager()
				.get("img/actors.pack", TextureAtlas.class);
		indicator0 = new Image(new TextureRegion(
				textureAtlas.findRegion("MainMenuLogo"), 0, 0, 40, 26));
		indicator0.setSize(80f, 52f);
		indicator0.setPosition(positionX, positionY);
		indicator1 = new Image(new TextureRegion(
				textureAtlas.findRegion("MainMenuLogo"), 40, 0, 40, 26));
		indicator1.setSize(80f, 52f);
		indicator1.setPosition(positionX, positionY);
		indicator1.setVisible(false);

		stage.addActor(background);
		stage.addActor(titleLabel);
		stage.addActor(newGameLabel);
		stage.addActor(musicLabel);
		stage.addActor(ExitLabel);
		stage.addActor(indicator0);
		stage.addActor(indicator1);

		currentSelection = 0;
		selected = false;

		GameManager.getInstance().playMusic("SuperBomberman-Title.ogg", true);
	}

	private void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !selected) {
			indicator0.setVisible(true);
			indicator1.setVisible(false);
			GameManager.getInstance().playSound("Pickup.ogg");
			currentSelection--;
			if (currentSelection < 0) {
				currentSelection += 3;
			}

			float newIndicatorY = positionY - currentSelection * 60f;

			MoveToAction moveToAction = new MoveToAction();
			moveToAction.setPosition(positionX, newIndicatorY);
			moveToAction.setDuration(0.2f);
			indicator0.clearActions();
			indicator0.addAction(moveToAction);
			indicator1.setPosition(positionX, newIndicatorY);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !selected) {
			indicator0.setVisible(true);
			indicator1.setVisible(false);
			GameManager.getInstance().playSound("Pickup.ogg");
			currentSelection++;
			if (currentSelection >= 3) {
				currentSelection -= 3;
			}
			float newIndicatorY = positionY - currentSelection * 60f;
			MoveToAction moveToAction = new MoveToAction();
			moveToAction.setPosition(positionX, newIndicatorY);
			moveToAction.setDuration(0.2f);
			indicator0.clearActions();
			indicator0.addAction(moveToAction);
			indicator1.setPosition(positionX, newIndicatorY);
		}

		if (!selected && (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
			GameManager.getInstance().playSound("Teleport.ogg");
			indicator0.setVisible(false);
			indicator1.setVisible(true);
			if (currentSelection == 1) {
				if (GameManager.audioEnabled == true) {
					GameManager.audioEnabled = false;
					GameManager.getInstance().stopMusic();
					musicLabel.setText("Music: OFF");
				} else {
					GameManager.audioEnabled = true;
					GameManager.getInstance()
							.playMusic("SuperBomberman-Title.ogg", true);
					musicLabel.setText("Music: ON");
				}
			} else {
				selected = true;
				RunnableAction runnableAction = new RunnableAction();
				runnableAction.setRunnable(new Runnable() {
					@Override
					public void run() {
						switch (currentSelection) {
							case 2 :
								Gdx.app.exit();
								break;
							case 0 :
							default :
								game.setScreen(new SelectScreen(game));
								break;
						}
					}
				});
				stage.addAction(new SequenceAction(Actions.delay(0.2f),
						Actions.fadeOut(1f), runnableAction));
			}
		}
	}

	@Override
	public void render(float delta) {
		handleInput();
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
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
		GameManager.getInstance().stopMusic();
		dispose();
	}

	@Override
	public void dispose() {
		backgroundTexture.dispose();
		stage.dispose();
		font.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
