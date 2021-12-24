package com.bom.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bom.game.screen.GameScreen;
import com.bom.game.screen.MenuScreen;
import com.bom.game.screen.VictoryScreen;

public class BomGame extends Game {
    public SpriteBatch batch;
    Texture img;
    public GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return batch;
    }
}
