package com.kingx.dungeons.generator;

public abstract class AbstractGenerator {

    public abstract boolean[][] build(int width, int height);

    public boolean[][][] buildLayered(int width, int height) {
        if (width % 4 != 0) {
            throw new IllegalArgumentException("Map's width has to be multiple of 4.");
        }
        boolean[][] original = build(width, height);

        int layerWidth = width / 4;

        boolean[][][] output = new boolean[4][layerWidth][height];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < output[i].length; j++) {
                for (int k = 0; k < output[i][j].length; k++) {

                    output[i][j][k] = original[i * layerWidth + j][k];

                }
            }
        }
        return output;
    }

}