package com.kingx.dungeons;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public final class Assets {
    public static boolean[][][] map;
    private static List<TextureAtlas> atlases = new ArrayList<TextureAtlas>();
    private static TextureAtlas current = null;
    private static int position = 1;
    private static HashMap<String, Array<AtlasRegion>> cachedTextures = new HashMap<String, Array<AtlasRegion>>();
    static {
        atlases.add(new TextureAtlas("data/textures/pixel/packed.atlas"));
        atlases.add(new TextureAtlas("data/textures/toon/packed.atlas"));
        current = atlases.get(position);
        try {
            map = (boolean[][][]) deserialize(Gdx.files.internal("data/map.dng"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object deserialize(FileHandle fileHandle) throws Exception {
        return new ObjectInputStream(fileHandle.read()).readObject();
    }

    public static AtlasRegion getTexture(String name, int index) {
        Array<AtlasRegion> result = getTextureArray(name);

        if (result.size > index) {
            return result.get(index);
        } else {
            throw new IllegalArgumentException("Texture [" + name + "_" + index + "] is not available.");
        }
    }

    public static Array<AtlasRegion> getTextureArray(String name) {
        Array<AtlasRegion> result = cachedTextures.get(name);
        if (result == null) {
            result = current.findRegions(name);
            cachedTextures.put(name, result);
        }

        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Texture [" + name + "] is not available.");
        }

    }

    public static void switchAtlas() {
        position = ++position < atlases.size() ? position : 0;
        System.out.println(position);
        current = atlases.get(position);
        cachedTextures.clear();
    }
}
