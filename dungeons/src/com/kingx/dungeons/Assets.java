package com.kingx.dungeons;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public final class Assets {

    public static Texture wall;
    static {
        // Texture.setEnforcePotImages(false);
        wall = new Texture(Gdx.files.internal("data/textures/wall.jpg"));
    }
}
