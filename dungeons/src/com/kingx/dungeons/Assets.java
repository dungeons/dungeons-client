package com.kingx.dungeons;

import java.io.ObjectInputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Assets {
    public static boolean[][] map;
    private static TextureAtlas atlas;
    private static HashMap<String, TextureRegion> cachedTextures = new HashMap<String, TextureRegion>();
    static {
        atlas = new TextureAtlas("data/textures/packed.atlas");

        try {
            map = (boolean[][]) deserialize(Gdx.files.internal("data/map.dng"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object deserialize(FileHandle fileHandle) throws Exception {
        return new ObjectInputStream(fileHandle.read()).readObject();
    }

    public static TextureRegion getTexture(String name) {
        TextureRegion result = cachedTextures.get(name);
        if (result == null) {
            result = atlas.findRegion(name);

            cachedTextures.put(name, result);
        }
        return result;
    }
}
