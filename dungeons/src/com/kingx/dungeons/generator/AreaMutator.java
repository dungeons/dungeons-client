package com.kingx.dungeons.generator;

public abstract class AreaMutator extends TerainMutator {

    protected final int area;

    public AreaMutator(int[][] terain, int type, int area) {
        super(terain, type);
        this.area = area;
    }

    protected void clean(int x, int y) {
        for (int i = x - area; i < x + area; i++) {
            for (int j = y - area; j < y + area; j++) {
                convert(i, j, type);
            }
        }
    }

}
