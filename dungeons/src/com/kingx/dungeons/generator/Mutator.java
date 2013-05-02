package com.kingx.dungeons.generator;

public abstract class Mutator {

    protected final TerainMutator mutator;

    public Mutator(TerainMutator t) {
        this.mutator = t;
    }

    public abstract void mutate(int x, int y);
}
