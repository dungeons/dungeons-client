package com.kingx.dungeons;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kingx.dungeons.App;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "dungeon";
		cfg.useGL20 = true;
		cfg.width = 540;
		cfg.height = 300;
		
		new LwjglApplication(new App(), cfg);
	}
}
