package com.kingx.dungeons.graphics;

import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public class MazeMap {

    private final boolean[][][] footprints;
    private int walls = -1;

    public static final float SIZE = 1f;

    public MazeMap(boolean[][][] map) {
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
                        if (!footprints[i][j][k]) {
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
            p.x = App.rand.nextInt(footprints.length);
            p.y = App.rand.nextInt(footprints[p.x].length);
        } while (!footprints[0][p.x][p.y]);

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
            p.x = App.rand.nextInt(footprints.length);
            p.y = App.rand.nextInt(footprints[p.x].length);
        } while (!footprints[0][p.x][p.y] || p.equals(start));
        return p;
    }

    public boolean[][] getFootprint() {
        return getFootprint(App.getCurrentFootprint());
    }

    public boolean[][] getFootprint(int i) {
        return footprints[i];
    }

    public int getFootprints() {
        return footprints.length;
    }

    public int getWidth() {
        return footprints[0].length;
    }

    public int getHeight() {
        return footprints[0][0].length;
    }

    public Vector3 getRandomPosition() {
        Int p = getRandomBlock();
        return new Vector3(SIZE * (p.x + 0.5f), SIZE * (p.y + 0.5f), 0);
    }

}
