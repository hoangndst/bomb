package com.bom.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bom.game.screen.GameScreen;

public class BomGame extends Game {
    public SpriteBatch batch;
    Texture img;

    public static final String TITLE = "Bomberman";
    public static final float PPM = 16;
    public static final float PPT = 16;
    public static final float WIDTH = 17 * PPM;
    public static final float HEIGHT = 13 * PPM;
    // public static final float SCALE = 3;
    public GameScreen gameScreen;

    // public static final short GROUND_BIT = 1;
    // public static final short PLAYER_BIT = 2;
    // public static final short BOMB_BIT = 4;
    // public static final short WALL_BIT = 8;
    // public static final short ENEMY_BIT = 16;
    // public static final short DESTROYED_BIT = 32;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
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
}
