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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
		Label victoryLabel = new Label("Victory", labelStyle);
		victoryLabel.setPosition((640 - victoryLabel.getWidth()) / 2, 250f);
		victoryLabel.setFontScale(1.5f);
		Label scoreLabel = new Label("Score", labelStyle);
		Label scoreLabel2 = new Label(String.format("%06d", GameManager.score),
				labelStyle);
		scoreLabel.setFontScale(0.9f);
		scoreLabel2.setFontScale(0.9f);
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		table.add(victoryLabel).expandX().padTop(150f);
		table.row();
		table.add(scoreLabel).expandX().padTop(10f);
		table.row();
		table.add(scoreLabel2).expandX();

		GameManager.getInstance().playMusic("Victory.ogg", false);

		// stage.addActor(v);
		stage.addActor(table);
		stage.addAction(Actions.sequence(Actions.delay(2f), Actions.fadeOut(2f),
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
