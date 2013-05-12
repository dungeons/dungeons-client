package com.kingx.dungeons.generator;

import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.Monster;

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

    public void setRemoved(int x, int y, boolean removed) {
        x += offx;
        y += offy;
        if (inBounds(x, y)) {
            terrain[x][y].setRemoved(removed);
        }
    }

    public void setCube(int x, int y, Block cube) {
        x += offx;
        y += offy;
        if (inBounds(x, y)) {
            terrain[x][y].setCube(cube);
        }
    }

    public void setMonster(int x, int y, Monster monster) {
        x += offx;
        y += offy;
        if (inBounds(x, y)) {
            terrain[x][y].setSpawn(monster);
        }
    }

    public boolean inBounds(int x, int y) {
        y = terrain[0].length - 1 - y;
        return x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
