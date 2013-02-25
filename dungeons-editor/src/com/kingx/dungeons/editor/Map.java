package com.kingx.dungeons.editor;

public class Map {

    static final int BLOCK_SIZE = 20;
    private boolean[][] maze;

    public Map(boolean[][] maze) {
        this.maze = maze;
    }

    public boolean[][] getMaze() {
        return maze;
    }

    public void switchBlock(int x, int y) {
        System.out.println(maze[x][y]);
        maze[x][y] = !maze[x][y];
        System.out.println(maze[x][y]);
    }

}
