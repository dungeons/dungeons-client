package com.kingx.dungeons.generator;

import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;

public class TerrainHolder {

    private final BlockPair[][] terrain;
    private final int offx;
    private final int offy;
    private final int height;
    private final int width;

    public TerrainHolder(BlockPair[][] terrain, int offx, int offy, int width, int height) {
        this.terrain = terrain;
        this.offx = offx;
        this.offy = offy;
        this.width = width;
        this.height = height;
    }

    public void set(BlockPair bp, int x, int y) {
        x += offx;
        y += offy;
        y = terrain[0].length - 1 - y;
        if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length) {
            terrain[x][y] = null;
        }
    }

    public void setVisible(int x, int y, boolean visible) {
        x += offx;
        y += offy;
        y = terrain[0].length - 1 - y;
        if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length) {
            terrain[x][y].setVisible(visible);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setFirst(int x, int y, Block block) {
        x += offx;
        y += offy;

        y = terrain[0].length - 1 - y;
        if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length) {
            terrain[x][y].setFirst(block);
        }
    }

}
