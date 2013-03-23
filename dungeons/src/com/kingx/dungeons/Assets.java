package com.kingx.dungeons;

import java.io.ObjectInputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class Assets {
    public static boolean[][][] map;
    private static TextureAtlas atlas;
    private static HashMap<String, Array<AtlasRegion>> cachedTextures = new HashMap<String, Array<AtlasRegion>>();
    static {
        atlas = new TextureAtlas("data/textures/packed.atlas");

        try {
            map = (boolean[][][]) deserialize(Gdx.files.internal("data/map.dng"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object deserialize(FileHandle fileHandle) throws Exception {
        return new ObjectInputStream(fileHandle.read()).readObject();
    }

    public static TextureRegion getTexture(String name, int index) {
        Array<AtlasRegion> result = cachedTextures.get(name);
        if (result == null) {
            result = atlas.findRegions(name);
            cachedTextures.put(name, result);
        }
        return result.get(index);
    }
}
