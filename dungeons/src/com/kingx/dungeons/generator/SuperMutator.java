package com.kingx.dungeons.generator;

import com.kingx.dungeons.BlockPair;

public abstract class SuperMutator implements Mutable {

    protected final BlockPair[][] terain;

    private final int regionWidth;
    private final int regionHeight;
    private final int min;
    private final int max;

    public SuperMutator(BlockPair[][] terain, int regionWidth, int regionHeight) {
        this(terain, regionWidth, regionHeight, 0, Integer.MAX_VALUE);
    }

    public SuperMutator(BlockPair[][] terain, int regionWidth, int regionHeight, int min, int max) {
        this.terain = terain;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
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

    @Override
    public void mutate(int x, int y) {
        if (isBounds(x, y)) {
            int halfWidth = regionWidth / 2;
            int halfHeight = regionHeight / 2;

            mutateInternal(new TerrainHolder(terain, x - halfWidth, y - halfHeight, regionWidth, regionHeight));
        }
    }

    protected abstract void mutateInternal(TerrainHolder terrain);

}
