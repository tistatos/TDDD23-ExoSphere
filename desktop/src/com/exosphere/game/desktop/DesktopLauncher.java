package com.exosphere.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.exosphere.game.Exosphere;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
//		config.width = 1280;
//		config.height = 720;

//		config.useGL30 = true;
		new LwjglApplication(new Exosphere(), config);
	}
}
