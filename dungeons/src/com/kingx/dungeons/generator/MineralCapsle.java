package com.kingx.dungeons.generator;

public class MineralCapsle extends AreaMutator {

    public MineralCapsle(int[][] terain, int type, int area) {
        super(terain, type, area);
    }

    @Override
    public void mutate(int x, int y) {
        clean(x, y);
    }

}
