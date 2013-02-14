package com.kingx.dungeons.entity;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.geom.Point;

public final class Maze extends RenderableEntity {

    private boolean[][] footprint;
    private int walls = -1;
    public Mesh poly;

    public static final float SIZE = 1f;

    public Maze(boolean[][] map) {
        super(0, 0, 0, SIZE * map.length, 0);
        this.footprint = map;
        walls = this.getWalls();
        poly = new PolyBuilder(this, SIZE).generate();
    }

    private int getWalls() {
        if (walls == -1) {
            int counter = 0;
            for (int i = 0; i < footprint.length; i++) {
                for (int j = 0; j < footprint[i].length; j++) {
                    if (!footprint[i][j]) {
                        counter++;
                    }
                }
            }
            return counter;
        } else {
            return walls;
        }
    }

    private static final class PolyBuilder {

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
                if (b)
                    count++;
            }
            // TODO Auto-generated method stub
            return count;
        }

        private final float WALL_SIZE;
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
                { 0, 1, 2, 3 } // Bottom
        };
        private static final float[][] normals = { { 0, -1, 0 }, // Front
                { 1, 0, 0 }, // Right
                { 0, 1, 0 }, // Back
                { -1, 0, 0 }, // Left
                { 0, 0, 1 }, // Top
                { 0, 0, 1 } // Bottom
        };

        private Maze maze;
        private ArrayList<Float> verts = new ArrayList<Float>();
        private ArrayList<Short> indices = new ArrayList<Short>();
        private int offVerts;
        private int offIndices;

        private PolyBuilder(Maze maze, float size) {
            this.maze = maze;
            WALL_SIZE = size;

            for (int i = 0; i < maze.footprint.length; i++) {
                for (int j = 0; j < maze.footprint[i].length; j++) {

                    float x = i * WALL_SIZE;
                    float y = j * WALL_SIZE;
                    if (!maze.footprint[i][j]) {
                        makeWall(x, y);
                    } else {
                        makeFloor(x, y);
                    }
                }
            }

        }

        private Mesh generate() {
            float[] outVerts = new float[verts.size()];
            short[] outIndices = new short[indices.size()];
            for (int i = 0; i < verts.size(); i++)
                outVerts[i] = verts.get(i);
            for (int i = 0; i < indices.size(); i++)
                outIndices[i] = indices.get(i);

            Mesh mesh = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(),
                    VertexAttribute.TexCoords(0), VertexAttribute.Normal());
            mesh.setVertices(outVerts);
            mesh.setIndices(outIndices);
            return mesh;
        }

        private boolean trigger = true;

        private void makeWall(float x, float y) {

            for (int i = 0; i < quads.length - 1; i++) {
                if (allowed[i]) {
                    makeQuad(x, y, i);
                }
            }
        }

        private void makeFloor(float x, float y) {
            for (int i = quads.length - 1; i < quads.length; i++) {
                if (allowed[i]) {
                    makeQuad(x, y, i);
                }
            }
        }

        int off = 0;

        private void makeQuad(float x, float y, int face) {
            for (int i = 0; i < quads[face].length; i++) {
                verts.add(positionOffset[quads[face][i]][0] * WALL_SIZE + x); // x position
                verts.add(positionOffset[quads[face][i]][1] * WALL_SIZE + y); // y position
                verts.add(positionOffset[quads[face][i]][2] * WALL_SIZE); // z position
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

    public void print() {
        Maze.print(footprint);
    }

    public static void print(boolean[][] maze) {
        String out = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                out += maze[i][j] ? "\u25A0 " : "\u25A1 ";
            }
            out += "\n";
        }
        System.out.println(out);
    }

    public static void print(boolean[][] maze, Point.Int current) {
        String out = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (current.equals(i, j)) {
                    out += "\u25A3 ";
                } else {
                    out += maze[i][j] ? "\u25A0 " : "\u25A1 ";
                }
            }
            out += "\n";
        }
        System.out.println(out);
    }

    protected void doRender(Camera cam) {
        // System.out.println(shader.isCompiled() + " maze " + shader.getLog());
        /*
         * shader.begin(); poly.render(shader, GL10.GL_TRIANGLES); shader.end();
         */

        // Assets.wall.bind();
        /*
         * shader.begin(); shader.setUniformMatrix("u_MVPMatrix", cam.combined); shader.setUniformMatrix("u_MVMatrix", cam.view);
         * shader.setUniformf("u_lightPos", App.getWanderer().position); // shader.setUniformi("u_texture", 0); poly.render(shader, GL10.GL_TRIANGLES);
         * shader.end();
         */
    }

    private Random rand = new Random();

    public Point.Int getRandomBlock() {
        Point.Int p = new Point.Int();
        do {
            p.x = rand.nextInt(footprint.length);
            p.y = rand.nextInt(footprint[p.x].length);
        } while (!footprint[p.x][p.y]);
        return p;
    }

    public Point.Int getRandomBlock(Point.Int start) {
        print(footprint);
        Point.Int p = new Point.Int();
        do {
            p.x = rand.nextInt(footprint.length);
            p.y = rand.nextInt(footprint[p.x].length);
        } while (!footprint[p.x][p.y] && p != start);
        return p;
    }

    @Override
    protected void initRender() {
    }

    public boolean[][] getFootprint() {
        return footprint;
    }

    public int getWidth() {
        return footprint.length;
    }

    public int getHeight() {
        return footprint[0].length;
    }

    @Override
    protected void doUpdate(float delta) {
    }

}
