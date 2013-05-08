package com.kingx.dungeons.graphics.cube;

import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.graphics.Terrain;

public class CubeDefaultSideFactory extends CubeSideFactory {

    public CubeDefaultSideFactory(Terrain maze) {
        super(maze, App.UNIT, true);
    }

    @Override
    protected Block getBlockInternal(BlockPair blockPair) {
        return Block.DEFAULT;
    }

}
