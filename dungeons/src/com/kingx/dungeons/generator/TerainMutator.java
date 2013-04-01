package com.kingx.dungeons.generator;

public abstract class TerainMutator {

    protected final int[][] terain;
    protected final int type;

    public TerainMutator(int[][] terain, int type) {
        this.terain = terain;
        this.type = type;
    }

    protected void convert(int x, int y, int type) {
        System.out.println(x + ":" + y);
        if (isBounds(x, y)) {
            terain[x][y] = type;
        }
    }

    private boolean isBounds(int x, int y) {
        if (x > 0 && x < terain.length) {
            if (y > 0 && y < terain[x].length) {
                return true;
            }
        }
        return false;
    }

    public abstract void mutate(int x, int y);
}
