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

    private Cube[][] cubes;
    private final ArrayList<CubeRegion> regions;

    public CubeFactory(MazeMap maze) {

        regions = new ArrayList<CubeRegion>();
        for (int i = 0; i < maze.getFootprints(); i++) {
            cubes = new Cube[maze.getFootprint(i).length + 1][maze.getFootprint(i)[0].length];
            for (int j = 0; j < cubes.length - 1; j++) {
                for (int k = 0; k < cubes[j].length; k++) {

                    float x = 0, y = 0, z = 0;

                    if (maze.getFootprint(i)[j][k] != 0) {
                        switch (i) {
                            case 0:
                                x = j;
                                y = k;
                                z = 0;
                                break;
                            case 1:
                                x = maze.getFootprint(i).length;
                                y = k;
                                z = -j;
                                break;
                            case 2:
                                x = maze.getFootprint(i).length - j;
                                y = k;
                                z = -maze.getFootprint(i).length;
                                break;
                            case 3:
                                x = 0;
                                y = k;
                                z = -maze.getFootprint(i).length + j;
                                break;
                        }
                        System.out.println(x * App.UNIT);
                        cubes[j][k] = makeCube(x * App.UNIT, y * App.UNIT, z * App.UNIT, maze.getFootprint(i)[j][k]);
                    }
                }
            }

            regions.add(new CubeRegion(cubes));

        }

        for (int i = 0; i < regions.size(); i++) {
            Cube[][] current = regions.get(i).getCubes();
            Cube[][] next = regions.get((i + 1) % regions.size()).getCubes();

            int last = current.length - 1;

            for (int k = 0; k < current[last].length; k++) {
                System.out.println("Assigning " + next[0][k] + "to" + current[last][k]);
                current[last][k] = next[0][k];
                //next[0][k] = current[last][k];
            }
        }
    }

    public ArrayList<CubeRegion> getCubeRegions() {
        return regions;
    }

    private Cube makeCube(float x, float y, float z, int type) {

        Cube c = new Cube();

        for (int i = 0; i < quads.length; i++) {

            TextureRegion texture = getTexture(type);
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
        cv.setPosition(positionOffset[quads[face][i]][0] * App.UNIT + x,  // x position
                       positionOffset[quads[face][i]][1] * App.UNIT + y,  // y position
                       positionOffset[quads[face][i]][2] * App.UNIT + z); // z position
        cv.setTexCoords(cords.x, cords.y);
        cv.setNormal(normals[face][0],  // x normal
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

    private static TextureRegion getTexture(int i) {
        switch (i) {
            case 1:
                return Assets.getTexture("wall", App.rand.nextInt(4));
            case 2:
                return Assets.getTexture("diamond", 0);
            default:
                return null;
        }
    }

}