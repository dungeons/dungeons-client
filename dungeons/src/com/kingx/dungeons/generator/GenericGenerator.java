package com.kingx.dungeons.generator;

public class GenericGenerator extends AbstractGenerator {
    private boolean[][] maze;

    @Override
    public boolean[][] build(int width, int height) {
        maze = new boolean[width][height];

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = j == maze[i].length - 2;
            }
        }
        return maze;
    }

}
