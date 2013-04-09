package com.kingx.dungeons.generator;

import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point.Int;

public abstract class AbstractGenerator {

    public abstract int[][] build(int width, int height);

    public int[][][] buildLayered(int width, int height) {
        if (width % 4 != 0) {
            throw new IllegalArgumentException("Map's width has to be multiple of 4.");
        }
        int[][] original = build(width, height);

        int layerWidth = width / 4;

        int[][][] output = new int[4][layerWidth][height];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < output[i].length; j++) {
                for (int k = 0; k < output[i][j].length; k++) {
                    output[i][j][k] = original[i * layerWidth + j][k];
                }
            }
        }
        return output;
    }

    protected Int getRandomPoint(int width, int height) {
        return new Int(App.rand.nextInt(width), App.rand.nextInt(height));
    }

}