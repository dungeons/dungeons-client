package com.kingx.dungeons;

import java.io.ObjectInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public final class Assets {

    public static Texture wall;
    public static boolean[][] map;
    static {
        wall = new Texture(Gdx.files.internal("data/textures/wall.jpg"));
        try {
            map = (boolean[][]) deserialize(Gdx.files.internal("data/map.dng"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object deserialize(FileHandle fileHandle) throws Exception {
        return new ObjectInputStream(fileHandle.read()).readObject();
    }
}
