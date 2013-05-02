package com.kingx.dungeons.generator;

import com.kingx.dungeons.BlockPair;

public abstract class TerainMutator<T> extends TerainMutator2 {

    protected final T type;

    public TerainMutator(BlockPair[][] terain, int min, int max, T type) {
        super(terain, min, max);
        this.type = type;
    }

    public TerainMutator(BlockPair[][] terain, T type) {
        super(terain);
        this.type = type;
    }

    protected abstract void convert(int x, int y);

}
