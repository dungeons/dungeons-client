package com.kingx.dungeons.entity;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.entity.graphics.PolyBuilder;
import com.kingx.dungeons.entity.graphics.Shader;
import com.kingx.dungeons.geom.Point;

public final class Maze extends RenderableEntity {

    private final boolean[][] footprint;
    private int walls = -1;
    private final Mesh poly;

    public static final float SIZE = 1f;

    public Maze(boolean[][] map, float size) {
        super(0, 0, 0, map.length * size, 0);
        this.footprint = map;
        walls = this.getWalls();
        Vector3 polySize = new Vector3(size, size, size);
        poly = new PolyBuilder(this, polySize).generate();

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

    private final Random rand = new Random();
    private ShaderProgram shader;

    public Point.Int getRandomBlock() {
        Point.Int p = new Point.Int();
        do {
            p.x = rand.nextInt(footprint.length);
            p.y = rand.nextInt(footprint[p.x].length);
        } while (!footprint[p.x][p.y]);
        return p;
    }

    public Point.Int getRandomBlock(Point.Int start) {
        Point.Int p = new Point.Int();
        do {
            p.x = rand.nextInt(footprint.length);
            p.y = rand.nextInt(footprint[p.x].length);
        } while (!footprint[p.x][p.y] || p.equals(start));
        return p;
    }

    @Override
    protected void initRender() {
        // render
        shader = Shader.getShader("normal");

    }

    @Override
    protected void doRender(Camera cam) {
        shader.begin();
        shader.setUniformMatrix("u_MVPMatrix", cam.combined);
        poly.render(shader, GL20.GL_TRIANGLES);
        shader.end();

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

    public Mesh getPoly() {
        return poly;
    }

    public void print() {
        Maze.print(footprint);
    }

    public static void print(boolean[][] maze) {
        Maze.print(maze, null);
    }

    public static void print(boolean[][] maze, Point.Int current) {
        String out = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (current != null && current.equals(i, j)) {
                    out += "\u25A3 ";
                } else {
                    out += maze[i][j] ? "\u25A0 " : "\u25A1 ";
                }
            }
            out += "\n";
        }
        System.out.println(out);
    }

}
