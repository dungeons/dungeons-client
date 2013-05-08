package com.kingx.dungeons.generator;

import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;

public class GlacierCaveMutator extends SuperMutator {

    public GlacierCaveMutator(BlockPair[][] terain, int regionWidth, int regionHeight, int min, int max) {
        super(terain, regionWidth, regionHeight, min, max);
    }

    public GlacierCaveMutator(BlockPair[][] terain, int regionWidth, int regionHeight) {
        super(terain, regionWidth, regionHeight);
    }

    @Override
    protected void mutateInternal(TerrainHolder terrain) {

        int o = 0;
        for (int i = 1; i < terrain.getWidth() - 1; i++) {
            terrain.setFirst(i, 0, Block.ICE);
        }
        o++;

        for (int i = 0; i < terrain.getWidth(); i++) {
            terrain.setFirst(i, 1, Block.ICE);
        }

        for (int i = 2; i < terrain.getWidth() - 2; i++) {
            terrain.setRemoved(i, 1, true);
        }
        o++;

        for (int i = o; i < terrain.getHeight() - 1; i++) {
            for (int j = 0; j < terrain.getWidth(); j++) {
                terrain.setFirst(j, i, Block.ICE);
                terrain.setRemoved(j, i, true);
            }
            o++;
        }

        for (int i = 0; i < terrain.getWidth(); i++) {
            terrain.setFirst(i, o, Block.ICE_PATH);
        }
    }
}
