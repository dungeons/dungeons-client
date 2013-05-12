package com.kingx.dungeons;

public class BlockPair {

    private Block cube;
    private Block mineral;
    private Monster spawn;
    private boolean removed = false;

    public BlockPair(Block cube, Block mineral, Monster spawn, boolean removed) {
        this.cube = cube;
        this.mineral = mineral;
        this.spawn = spawn;
        this.removed = removed;
    }

    public Block getCube() {
        return cube;
    }

    public void setCube(Block cube) {
        this.cube = cube;
    }

    public Block getMineral() {
        return mineral;
    }

    public void setMineral(Block mineral) {
        this.mineral = mineral;
    }

    public Monster getSpawn() {
        return spawn;
    }

    public void setSpawn(Monster spawn) {
        this.spawn = spawn;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}
