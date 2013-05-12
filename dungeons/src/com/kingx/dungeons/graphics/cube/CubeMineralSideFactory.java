package com.kingx.dungeons.graphics.cube;

import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.graphics.Terrain;

public class CubeMineralSideFactory extends CubeSideFactory {

    public CubeMineralSideFactory(Terrain maze) {
        super(maze, App.UNIT + 0.001f, false);
    }

    @Override
    protected Block getBlockInternal(BlockPair blockPair) {
        return blockPair.getMineral();
    }

}
