package com.kingx.dungeons.entity.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.entity.MazeShadow;

public final class PolyBuilder {

    private static final Boolean[] allowed = { true,// Front
            true,// Right
            true,// Back
            true,// Left
            true,// Top
            true // Bottom
    };

    private static final int allowedCount = getAllowedCount();

    private static int getAllowedCount() {
        int count = 0;
        for (Boolean b : allowed) {
            if (b) {
                count++;
            }
        }
        return count;
    }

    private final Vector3 WALL_SIZE;
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

    final float[][] cubeColorData = {
            // Front face (red)
            { 1.0f, 0.0f, 0.0f, 1.0f },
            // Right face (green)
            { 0.0f, 1.0f, 0.0f, 1.0f },
            // Back face (blue)
            { 0.0f, 0.0f, 1.0f, 1.0f },
            // Left face (yellow)
            { 1.0f, 1.0f, 0.0f, 1.0f },
            // Top face (cyan)
            { 0.0f, 1.0f, 1.0f, 1.0f },
            // Bottom face (magenta)
            { 1.0f, 0.0f, 1.0f, 1.0f } };

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

    private ArrayList<Float> verts = new ArrayList<Float>();
    private ArrayList<Short> indices = new ArrayList<Short>();

    public PolyBuilder(MazeShadow maze, Vector3 wallSize) {
        WALL_SIZE = wallSize;

        for (int i = 0; i < maze.getFootprint().length; i++) {
            for (int j = 0; j < maze.getFootprint().length; j++) {

                float x = i * WALL_SIZE.x;
                float y = j * WALL_SIZE.y;
                if (!maze.getFootprint()[i][j]) {
                    makeWall(maze.getPositionX() + x, maze.getPositionY() + y, maze.getPositionZ());
                } else {
                    // makeFloor(x,y);
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

        Mesh mesh = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(),
                VertexAttribute.TexCoords(0), VertexAttribute.Normal());
        mesh.setVertices(outVerts);
        mesh.setIndices(outIndices);
        return mesh;
    }

    private boolean trigger = true;

    private void makeWall(float x, float y, float z) {

        for (int i = 0; i < quads.length; i++) {
            if (allowed[i]) {
                makeQuad(x, y, z, i);
            }
        }
    }

    private void makeFloor(float x, float y, float z) {
        for (int i = quads.length - 1; i < quads.length; i++) {
            if (allowed[i]) {
                makeQuad(x, y, z, i);
            }
        }
    }

    int off = 0;

    private void makeQuad(float x, float y, float z, int face) {
        for (int i = 0; i < quads[face].length; i++) {
            verts.add(positionOffset[quads[face][i]][0] * WALL_SIZE.x + x); // x position
            verts.add(positionOffset[quads[face][i]][1] * WALL_SIZE.y + y); // y position
            verts.add(positionOffset[quads[face][i]][2] * WALL_SIZE.z + z); // z position
            verts.add(cubeColorData[face][0]); // r color
            verts.add(cubeColorData[face][1]); // g color
            verts.add(cubeColorData[face][2]); // b color
            verts.add(cubeColorData[face][3]); // a color
            verts.add(textureOffset[i][0]); // tex-x position
            verts.add(textureOffset[i][1]); // tex-y position
            verts.add(normals[face][0]); // x normal
            verts.add(normals[face][1]); // y normal
            verts.add(normals[face][2]); // z normal
        }

        indices.add((short) (off + 0));
        indices.add((short) (off + 1));
        indices.add((short) (off + 3));

        indices.add((short) (off + 3));
        indices.add((short) (off + 1));
        indices.add((short) (off + 2));

        off += 4;

    }

}