package com.kingx.dungeons.generator;

import com.kingx.dungeons.BlockPair;

public abstract class TerainMutator2 {

    protected final BlockPair[][] terain;

    private int min = 0;
    private int max = Integer.MAX_VALUE;

    public TerainMutator2(BlockPair[][] terain) {
        this.terain = terain;
    }

    public TerainMutator2(BlockPair[][] terain, int min, int max) {
        this.terain = terain;
        this.min = min;
        this.max = max;
    }

    protected boolean isBounds(int x, int y) {
        if (x > 0 && x < terain.length) {
            if (y > min && y < Math.min(terain[x].length, max)) {
                return true;
            }
        }
        return false;
    }

}
