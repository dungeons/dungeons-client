package com.kingx.dungeons.editor;

public class Map {

    public static final int BLOCK_SIZE = 20;

    private final boolean[][] maze;

    public Map(boolean[][] maze) {
        if (maze.length % 4 != 0) {
            throw new IllegalArgumentException("Map's width has to be multiple of 4.");
        }
        this.maze = maze;
    }

    public boolean[][] getMaze() {
        return maze;
    }

    public boolean[][][] getStructuredMaze() {
        int width = maze.length / 4;
        boolean[][][] output = new boolean[4][width][maze[0].length];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < output[i][j].length; k++) {
                    output[i][j][k] = maze[i * width + j][k];
                }
            }
        }
        return output;
    }

    public void switchBlock(int x, int y) {
        maze[x][y] = !maze[x][y];
    }

}
