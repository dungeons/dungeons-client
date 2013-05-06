package com.kingx.dungeons.generator;

import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;

public class BlockMutator extends TerainMutator<Block> {

    public BlockMutator(BlockPair[][] terain, Block type, int min, int max) {
        super(terain, min, max, type);
    }

    public BlockMutator(BlockPair[][] terain, Block type) {
        super(terain, type);
    }

    @Override
    protected void convert(int x, int y) {
        if (isBounds(x, y)) {
            if (terain[x][y] != null) {
                terain[x][y].setFirst(type);
            }
        }
    }

}
