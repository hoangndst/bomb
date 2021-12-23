package com.bom.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bom.game.BomGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 17 * 48;
		config.height = 13 * 48;
		config.resizable = false;
		new LwjglApplication(new BomGame(), config);
	}
}
