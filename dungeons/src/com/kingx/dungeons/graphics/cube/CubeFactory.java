package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;

public final class CubeFactory {

    private static final int pos = 1;
    private static final float[][] positionOffset = { { 0, 0, 0 }, // 0/7
            { pos, 0, 0 }, // 1/7
            { pos, pos, 0 }, // 2/7
            { 0, pos, 0 }, // 3/7
            { 0, 0, pos }, // 4/7
            { pos, 0, pos }, // 5/7
            { pos, pos, pos }, // 6/7
            { 0, pos, pos } // 7/7
    };

    private static final int[][] quads = { { 3, 2, 1, 0 }, // Back
            { 2, 6, 5, 1 }, // Left
            { 6, 7, 4, 5 }, // Front
            { 7, 3, 0, 4 }, // Right
            { 7, 6, 2, 3 }, // Top
            { 0, 1, 5, 4 } // Bottom
    };
    private static final float[][] normals = { { 0, -1, 0 }, // Front
            { 1, 0, 0 }, // Right
            { 0, 1, 0 }, // Back
            { -1, 0, 0 }, // Left
            { 0, 0, 1 }, // Top
            { 0, 0, -1 } // Bottom
    };

    public static Cube makeCube(int region, float x, float y, float z, float size, Block type, int xInMap, int yInMap) {

        Cube c = new Cube(region, xInMap, yInMap, type);

        for (int i = 0; i < quads.length; i++) {

            TextureRegion texture = getTexture(type, i);
            c.addVerts(makeQuad(x, y, z, size, i, texture, Colors.random()), CubeSideType.values()[i]);
        }

        c.computeBoundaries();

        return c;

    }

    public static SimpleCube makeCube(float x, float y, float z, float size, Color color) {

        SimpleCube c = new SimpleCube();

        for (int i = 0; i < quads.length; i++) {
            c.addVerts(makeQuad(x, y, z, size, i, null, color), CubeSideType.values()[i]);
        }
        return c;

    }

    private static ArrayList<CubeVertex> makeQuad(float x, float y, float z, float size, int face, TextureRegion texture, Color color) {
        ArrayList<CubeVertex> quad = new ArrayList<CubeVertex>(6);
        for (int i = 0; i < quads[face].length; i++) {
            quad.add(makeVertex(x, y, z, size, i, face, texture, color));
        }
        return quad;
    }

    private static CubeVertex makeVertex(float x, float y, float z, float size, int i, int face, TextureRegion texture, Color color) {
        // @formatter:off
        CubeVertex cv = new CubeVertex();
        float offset = (App.UNIT-size)/2f;
        cv.setPosition(positionOffset[quads[face][i]][0] * size + x+offset,  // x position
                       positionOffset[quads[face][i]][1] * size + y+offset,  // y position
                       positionOffset[quads[face][i]][2] * size + z+offset); // z position
        if(texture != null){
        Vector2 cords = getTextureCoordinates(i, texture);
        cv.setTexCoords(cords.x, cords.y);
        }
        cv.setNormal(normals[face][0],  // x normal
                     normals[face][1],  // y normal
                     normals[face][2]); // z normal


        if(color != null){
       cv.setColor(color.r, color.g, color.b, color.a);
        }
        // @formatter:on
        return cv;
    }

    private static Vector2 getTextureCoordinates(int i, TextureRegion tr) {
        if (tr == null) {
            return new Vector2(0, 0);
        }
        switch (i) {
            case 0:
                return new Vector2(tr.getU(), tr.getV());
            case 1:
                return new Vector2(tr.getU2(), tr.getV());
            case 2:
                return new Vector2(tr.getU2(), tr.getV2());
            case 3:
                return new Vector2(tr.getU(), tr.getV2());
        }
        return null;
    }

    private static TextureRegion getTexture(Block block, int face) {
        return block != null ? Assets.getTexture(block.getTextureName(CubeSideType.values()[face].ordinal()), 0) : null;
    }

}