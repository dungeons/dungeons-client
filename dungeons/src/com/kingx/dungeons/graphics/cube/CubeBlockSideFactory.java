package com.kingx.dungeons.graphics.cube;

import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.graphics.Terrain;

public class CubeBlockSideFactory extends CubeSideFactory {

    public CubeBlockSideFactory(Terrain maze) {
        super(maze, App.UNIT);
    }

    @Override
    protected Block getBlock(BlockPair blockPair) {
        return blockPair != null ? blockPair.getFirst() : null;
    }

}
