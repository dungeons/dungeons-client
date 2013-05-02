package com.kingx.dungeons.generator;

public class AreaMutator extends Mutator {

    private final int area;

    public AreaMutator(TerainMutator t, int area) {
        super(t);
        this.area = area;
    }

    @Override
    public void mutate(int x, int y) {
        for (int i = x - area; i < x + area; i++) {
            for (int j = y - area; j < y + area; j++) {
                this.mutator.convert(i, j);
            }
        }
    }

}
