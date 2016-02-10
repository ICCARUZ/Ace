package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Ace;

public class DesktopLauncher 
{	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320; // 640
		config.height = 180; // 360
		config.resizable = false;
		config.vSyncEnabled = false;
		config.addIcon("./res/art/icon.png", Files.FileType.Internal);
		config.foregroundFPS = 0;
		new LwjglApplication(new Ace(), config);
	}
}
