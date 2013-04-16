package com.kingx.dungeons.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.kingx.dungeons.App;

public class Main {
    public static void main(String[] args) {
        Settings config = new Settings();
        config.maxWidth = 512;
        config.maxHeight = 512;
        config.paddingY = 0;
        config.paddingX = 0;
        TexturePacker2.process(config, "unprocessed/textures/", "../dungeons-android/assets/data/textures", "packed");

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "dungeon";
        cfg.useGL20 = true;
        cfg.width = 640;
        cfg.height = 480;

        new LwjglApplication(new App(args), cfg);
    }
}
