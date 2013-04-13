package com.kingx.dungeons.graphics;

import com.badlogic.gdx.math.Vector2;
import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public class Terrain {

    private final int[][][] footprints;
    private int walls = -1;

    public Terrain(int[][][] map) {
        this.footprints = map;
        walls = this.getWalls();
    }

    /**
     * Returns number of non-walkable blocks
     * 
     * @return non-walkable blocks count
     */
    public int getWalls() {
        if (walls == -1) {
            int counter = 0;
            for (int i = 0; i < footprints.length; i++) {
                for (int j = 0; j < footprints[i].length; j++) {
                    for (int k = 0; k < footprints[i][j].length; k++) {
                        if (footprints[i][j][k] != 0) {
                            counter++;
                        }
                    }
                }
            }
            return counter;
        } else {
            return walls;
        }
    }

    /**
     * Searches for a walkable block
     * 
     * @return first walkable block
     */
    public Point.Int getRandomBlock() {
        Point.Int p = new Point.Int();
        do {
            p.x = App.rand.nextInt(footprints[App.getCurrentView()].length);
            p.y = App.rand.nextInt(footprints[App.getCurrentView()][p.x].length);
        } while (footprints[0][p.x][p.y] != 0);

        return p;
    }

    /**
     * Searches for a walkable block different from block specified
     * 
     * @return first walkable block
     */
    public Point.Int getRandomBlock(Point.Int start) {
        Point.Int p = new Point.Int();
        do {
            p.x = App.rand.nextInt(footprints[App.getCurrentView()].length);
            p.y = App.rand.nextInt(footprints[App.getCurrentView()][p.x].length);
        } while (footprints[0][p.x][p.y] != 0 || p.equals(start));
        return p;
    }

    /**
     * Searches for a walkable block different from block specified
     * 
     * @return first walkable block
     */
    public Point.Int getRandomBlock(int maxx, int maxy) {
        Point.Int p = new Point.Int();
        do {
            int[][] current = footprints[App.getCurrentView()];
            p.x = App.rand.nextInt(Math.min(maxx, current.length));
            maxy = Math.min(maxy, current[p.x].length);
            p.y = current[p.x].length - maxy + App.rand.nextInt(maxy);
            System.out.println(current[p.x].length);
        } while (footprints[0][p.x][p.y] != 0);
        return p;
    }

    public int[][] getFootprint() {
        return getFootprint(App.getCurrentView());
    }

    public int[][] getNextFootprint() {
        return getFootprint(App.getCurrentView() + 1);
    }

    public int[][] getFootprint(int i) {
        return footprints[i % footprints.length];
    }

    public int getFootprints() {
        return footprints.length;
    }

    public int getWidth() {
        return footprints[0].length + 1;
    }

    public int getHeight() {
        return footprints[0][0].length;
    }

    public Vector2 getRandomPosition() {
        Int p = getRandomBlock();
        return new Vector2(App.UNIT * (p.x + 0.5f), App.UNIT * (p.y + 0.5f));
    }

    public Vector2 getRandomPosition(int maxx, int maxy) {
        Int p = getRandomBlock(maxx, maxy);
        return new Vector2(App.UNIT * (p.x + 0.5f), App.UNIT * (p.y + 0.5f));
    }

    public int getFootprint(int x, int y, int z) {
        int[][] footprint = this.getFootprint(x);
        if (y == footprint.length) {
            footprint = App.getMap().getFootprint(x + 1);
            return footprint[0][z];
        } else {
            return footprint[y][z];
        }
    }

    public void setFootprint(int x, int y, int z, int value) {
        int[][] footprint = this.getFootprint(x);
        if (y == footprint.length) {
            footprint = App.getMap().getFootprint(x + 1);
            footprint[0][z] = value;
        } else {
            footprint[y][z] = value;
        }
    }

}
