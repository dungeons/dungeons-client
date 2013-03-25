package com.kingx.dungeons.geom;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.graphics.MazeMap;

public final class GroundFactory {

    private final float WALL_SIZE;
    private final int VERTS_PER_QUAD = 4;

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
    private int vertsOffset = 0;

    public GroundFactory(MazeMap maze, float mazeWallSize) {
        WALL_SIZE = mazeWallSize;
        for (int i = 0; i < maze.getFootprints(); i++) {
            for (int j = 1; j < maze.getFootprint(i).length; j++) {
                for (int k = 0; k < maze.getFootprint(i)[j].length; k++) {

                    TextureRegion texture = getWallTexture(App.rand.nextInt(4));
                    float x = 0, y = 0, z = 0;

                    float offset = 0f;
                    switch (i) {
                        case 0:
                            x = j;
                            y = k;
                            z = -offset;
                            makeQuad(x * WALL_SIZE, y * WALL_SIZE, z * WALL_SIZE, 5, texture);
                            break;
                        case 1:
                            x = maze.getFootprint(i).length;
                            y = k;
                            z = -j - offset;
                            makeQuad(x * WALL_SIZE, y * WALL_SIZE, z * WALL_SIZE, 3, texture);
                            break;
                        case 2:
                            x = maze.getFootprint(i).length - j;
                            y = k;
                            z = -maze.getFootprint(i).length - offset;
                            makeQuad(x * WALL_SIZE, y * WALL_SIZE, z * WALL_SIZE, 4, texture);
                            break;
                        case 3:
                            x = 0;
                            y = k;
                            z = -maze.getFootprint(i).length + j - offset;
                            makeQuad(x * WALL_SIZE, y * WALL_SIZE, z * WALL_SIZE, 1, texture);
                            break;
                    }

                }
            }
        }
    }

    public Mesh generate() {
        float[] outVerts = new float[verts.size()];
        short[] outIndices = new short[indices.size()];
        for (int i = 0; i < verts.size(); i++) {
            outVerts[i] = verts.get(i);
        }
        for (int i = 0; i < indices.size(); i++) {
            outIndices[i] = indices.get(i);
        }
        Mesh mesh = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position(), VertexAttribute.TexCoords(0), VertexAttribute.Normal());

        mesh.setVertices(outVerts);
        mesh.setIndices(outIndices);
        return mesh;
    }

    private void makeQuad(float x, float y, float z, int face, TextureRegion texture) {
        for (int i = 0; i < quads[face].length; i++) {
            Vector2 cords = getTextureCoordinates(i, texture);
            verts.add(positionOffset[quads[face][i]][0] * WALL_SIZE + x); // x position
            verts.add(positionOffset[quads[face][i]][1] * WALL_SIZE + y); // y position
            verts.add(positionOffset[quads[face][i]][2] * WALL_SIZE + z); // z position
            verts.add(cords.x); // tex-x position
            verts.add(cords.y); // tex-y position
            verts.add(normals[face][0]); // x normal
            verts.add(normals[face][1]); // y normal
            verts.add(normals[face][2]); // z normal
        }

        indices.add((short) (vertsOffset + 0));
        indices.add((short) (vertsOffset + 1));
        indices.add((short) (vertsOffset + 3));

        indices.add((short) (vertsOffset + 3));
        indices.add((short) (vertsOffset + 1));
        indices.add((short) (vertsOffset + 2));

        vertsOffset += VERTS_PER_QUAD;
    }

    private Vector2 getTextureCoordinates(int i, TextureRegion tr) {
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

    private TextureRegion getWallTexture(int i) {
        return Assets.getTexture("floor", i);
    }

}