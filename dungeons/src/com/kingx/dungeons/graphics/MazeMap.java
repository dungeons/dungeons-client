package com.kingx.dungeons.graphics;

import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point;

public class MazeMap {

    private final boolean[][] footprint;
    private int walls = -1;

    public static final float SIZE = 1f;

    public MazeMap(boolean[][] map) {
        this.footprint = map;
        walls = this.getWalls();
    }

    /**
     * Returns number of non-walkable blocks
     * 
     * @return non-walkable blocks count
     */
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

    /**
     * Searches for a walkable block
     * 
     * @return first walkable block
     */
    public Point.Int getRandomBlock() {
        Point.Int p = new Point.Int();
        do {
            p.x = App.rand.nextInt(footprint.length);
            p.y = App.rand.nextInt(footprint[p.x].length);
        } while (!footprint[p.x][p.y]);

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
            p.x = App.rand.nextInt(footprint.length);
            p.y = App.rand.nextInt(footprint[p.x].length);
        } while (!footprint[p.x][p.y] || p.equals(start));
        return p;
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

    public void print() {
        print(footprint);
    }

    public static void print(boolean[][] maze) {
        print(maze, null);
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
