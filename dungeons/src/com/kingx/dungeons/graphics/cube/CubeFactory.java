package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.MazeMap;

public final class CubeFactory {

    private final float WALL_SIZE;
    private final static int VERTS_PER_QUAD = 4;

    private static final float[][] positionOffset = { { 0, 0, 0 }, // 0/7
            { 1, 0, 0 }, // 1/7
            { 1, 1, 0 }, // 2/7
            { 0, 1, 0 }, // 3/7
            { 0, 0, 1 }, // 4/7
            { 1, 0, 1 }, // 5/7
            { 1, 1, 1 }, // 6/7
            { 0, 1, 1 } // 7/7
    };

    private static final int[][] quads = { { 0, 1, 5, 4 }, // Front
            { 1, 2, 6, 5 }, // Right
            { 2, 3, 7, 6 }, // Back
            { 3, 0, 4, 7 }, // Left
            { 4, 5, 6, 7 }, // Top
            { 3, 2, 1, 0 } // Bottom
    };
    private static final float[][] normals = { { 0, -1, 0 }, // Front
            { 1, 0, 0 }, // Right
            { 0, 1, 0 }, // Back
            { -1, 0, 0 }, // Left
            { 0, 0, 1 }, // Top
            { 0, 0, -1 } // Bottom
    };

    private final ArrayList<Float> verts = new ArrayList<Float>();
    private final ArrayList<Short> indices = new ArrayList<Short>();
    private final int vertsOffset = 0;
    private ArrayList<Cube> cubes;
    private final ArrayList<CubeRegion> regions;

    public CubeFactory(MazeMap maze, float wallSize) {

        WALL_SIZE = wallSize;

        regions = new ArrayList<CubeRegion>();
        for (int i = 0; i < maze.getFootprints(); i++) {
            CubeRegion region = new CubeRegion();
            cubes = new ArrayList<Cube>();
            for (int j = 0; j < maze.getFootprint(i).length; j++) {
                for (int k = 0; k < maze.getFootprint(i)[j].length; k++) {

                    float x = 0, y = 0, z = 0;

                    float offset = 0f;

                    if (!maze.getFootprint(i)[j][k]) {
                        switch (i) {
                            case 0:
                                x = j;
                                y = k;
                                z = -offset;
                                break;
                            case 1:
                                x = maze.getFootprint(i).length;
                                y = k;
                                z = -j - offset;
                                break;
                            case 2:

                                x = maze.getFootprint(i).length - j;
                                y = k;
                                z = -maze.getFootprint(i).length - offset;
                                break;
                            case 3:

                                x = 0;
                                y = k;
                                z = -maze.getFootprint(i).length + j - offset;
                                break;
                        }

                        cubes.add(makeCube(x * WALL_SIZE, y * WALL_SIZE, z * WALL_SIZE));

                    }
                }
            }
            region.setCubes(cubes);
            regions.add(region);

        }

    }

    public ArrayList<CubeRegion> getCubeRegions() {
        return regions;
    }

    private Cube makeCube(float x, float y, float z) {

        Cube c = new Cube();

        for (int i = 0; i < quads.length; i++) {
            TextureRegion texture = getWallTexture(App.rand.nextInt(4));
            c.addVerts(makeQuad(x, y, z, i, texture));
        }

        c.make();
        return c;

    }

    private ArrayList<CubeVertex> makeQuad(float x, float y, float z, int face, TextureRegion texture) {
        ArrayList<CubeVertex> quad = new ArrayList<CubeVertex>(6);
        for (int i = 0; i < quads[face].length; i++) {
            quad.add(makeVertex(x, y, z, i, face, texture));
        }
        return quad;
    }

    private CubeVertex makeVertex(float x, float y, float z, int i, int face, TextureRegion texture) {
        // @formatter:off
        CubeVertex cv = new CubeVertex();
        Vector2 cords = getTextureCoordinates(i, texture);
        cv.setPosition(positionOffset[quads[face][i]][0] * WALL_SIZE + x,  // x position
                positionOffset[quads[face][i]][1] * WALL_SIZE + y,  // y position
                positionOffset[quads[face][i]][2] * WALL_SIZE + z); // z position
        cv.setTexCoords(cords.x, cords.y);
        cv.setNormal(normals[face][0], // x normal
               normals[face][1],  // y normal
               normals[face][2]); // z normal

        Color c = Colors.random();
       cv.setColor(c.r, c.g, c.b, c.a);
        // @formatter:on
        return cv;
    }

    private static Vector2 getTextureCoordinates(int i, TextureRegion tr) {
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

    private static TextureRegion getWallTexture(int i) {
        return Assets.getTexture("wall", i);
    }

}