package com.kingx.dungeons;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "dungeon";
        cfg.useGL20 = true;
        cfg.width = 640;
        cfg.height = 480;

        new LwjglApplication(new App(), cfg);
    }
}
