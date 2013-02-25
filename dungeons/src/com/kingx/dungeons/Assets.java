package com.kingx.dungeons;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public final class Assets {

    public static Texture wall;
    public static boolean[][] map;
    static {
        // Texture.setEnforcePotImages(false);
        wall = new Texture(Gdx.files.internal("data/textures/wall.jpg"));
        try {
            map = (boolean[][]) deserialize(Gdx.files.internal("data/map.dng"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static Object deserialize(FileHandle fileHandle) throws IOException, ClassNotFoundException {
        return new ObjectInputStream(fileHandle.read()).readObject();
    }
}
