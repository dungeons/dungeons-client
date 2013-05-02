package com.kingx.dungeons.generator;

public class SpotMutator extends Mutator {

    public SpotMutator(TerainMutator t) {
        super(t);
    }

    @Override
    public void mutate(int x, int y) {
        this.mutator.convert(x, y);
    }

}
