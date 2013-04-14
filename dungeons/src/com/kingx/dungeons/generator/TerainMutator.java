package com.kingx.dungeons.generator;

public abstract class TerainMutator {

    protected final int[][] terain;
    protected final int type;

    private int min = 0;
    private int max = Integer.MAX_VALUE;

    public TerainMutator(int[][] terain, int type) {
        this.terain = terain;
        this.type = type;
    }

    public TerainMutator(int[][] terain, int type, int min, int max) {
        this.terain = terain;
        this.type = type;
        this.min = min;
        this.max = max;
    }

    protected void convert(int x, int y, int type) {
        if (isBounds(x, y)) {
            terain[x][y] = type;
        }
    }

    private boolean isBounds(int x, int y) {
        if (x > 0 && x < terain.length) {
            if (y > min && y < Math.min(terain[x].length, max)) {
                return true;
            }
        }
        return false;
    }

    public abstract void mutate(int x, int y);
}
