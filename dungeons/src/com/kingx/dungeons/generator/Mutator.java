package com.kingx.dungeons.generator;

public abstract class Mutator implements Mutable {

    protected final TerainMutator mutator;

    public Mutator(TerainMutator t) {
        this.mutator = t;
    }

}
