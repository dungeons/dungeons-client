package com.kingx.dungeons.generator;

import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.Monster;

public class GlacierCaveMutator extends SuperMutator {

    public GlacierCaveMutator(BlockPair[][] terain, int regionWidth, int regionHeight, int min, int max) {
        super(terain, regionWidth, regionHeight, min, max);
    }

    public GlacierCaveMutator(BlockPair[][] terain, int regionWidth, int regionHeight) {
        super(terain, regionWidth, regionHeight);
    }

    @Override
    protected void mutateInternal(TerrainHolder terrain) {

        System.out.println("MUTATE");

        int o = 0;
        for (int i = 1; i < terrain.getWidth() - 1; i++) {
            terrain.setCube(i, 0, Block.ICE);
        }
        o++;

        for (int i = 0; i < terrain.getWidth(); i++) {
            terrain.setCube(i, 1, Block.ICE);
        }

        for (int i = 2; i < terrain.getWidth() - 2; i++) {
            terrain.setRemoved(i, 1, true);
        }
        o++;

        for (int i = o; i < terrain.getHeight() - 1; i++) {
            for (int j = 0; j < terrain.getWidth(); j++) {
                terrain.setCube(j, i, Block.ICE);
                terrain.setRemoved(j, i, true);
            }
            o++;
        }
        terrain.setMonster(0, o - 1, Monster.ZOMBIE);

        for (int i = 0; i < terrain.getWidth(); i++) {
            terrain.setCube(i, o, Block.ICE_PATH);
        }
    }
}
