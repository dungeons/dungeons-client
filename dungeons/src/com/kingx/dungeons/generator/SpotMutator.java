package com.kingx.dungeons.generator;

public class SpotMutator extends TerainMutator {

    public SpotMutator(int[][] terain, int type) {
        super(terain, type);
    }

    @Override
    public void mutate(int x, int y) {
        convert(x, y, type);
    }

}