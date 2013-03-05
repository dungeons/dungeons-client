package com.kingx.dungeons.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.geom.MazePoly;

public final class MazePolygon {

    private final Vector3 WALL_SIZE;
    private final int VERTS_PER_QUAD = 4;

    private static final Boolean[] allowed = { true,// Front
            true,// Right
            true,// Back
            true,// Left
            true,// Top
            true // Bottom
    };

    private static final float[][] positionOffset = { { 0, 0, 0 }, // 0/7
            { 1, 0, 0 }, // 1/7
            { 1, 1, 0 }, // 2/7
            { 0, 1, 0 }, // 3/7
            { 0, 0, 1 }, // 4/7
            { 1, 0, 1 }, // 5/7
            { 1, 1, 1 }, // 6/7
            { 0, 1, 1 } // 7/7
    };
    private static final float[][] textureOffset = { { 0, 0 }, // 0/3
            { 1, 0 }, // 1/3
            { 1, 1 }, // 2/3
            { 0, 1 } // 3/3
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

    public MazePolygon(MazeMap maze, Vector3 wallSize) {
        WALL_SIZE = wallSize;

        for (int i = 0; i < maze.getFootprint().length; i++) {
            for (int j = 0; j < maze.getFootprint().length; j++) {

                float x = i * WALL_SIZE.x;
                float y = j * WALL_SIZE.y;
                if (!maze.getFootprint()[i][j]) {
                    makeWall(x, y, 0);
                }
            }
        }
    }

    public MazePoly generate() {
        float[] outVerts = new float[verts.size()];
        short[] outIndices = new short[indices.size()];
        for (int i = 0; i < verts.size(); i++) {
            outVerts[i] = verts.get(i);
        }
        for (int i = 0; i < indices.size(); i++) {
            outIndices[i] = indices.get(i);
        }
        Mesh mesh = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position(), VertexAttribute.TexCoords(0), VertexAttribute.Normal());

        return new MazePoly(mesh, outVerts, outIndices);
    }

    private void makeWall(float x, float y, float z) {
        for (int i = 0; i < quads.length; i++) {
            if (allowed[i]) {
                makeQuad(x, y, z, i);
            }
        }
    }

    private void makeQuad(float x, float y, float z, int face) {
        for (int i = 0; i < quads[face].length; i++) {
            verts.add(positionOffset[quads[face][i]][0] * WALL_SIZE.x + x); // x position
            verts.add(positionOffset[quads[face][i]][1] * WALL_SIZE.y + y); // y position
            verts.add(positionOffset[quads[face][i]][2] * WALL_SIZE.z + z); // z position
            verts.add(textureOffset[i][0]); // tex-x position
            verts.add(textureOffset[i][1]); // tex-y position
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

}