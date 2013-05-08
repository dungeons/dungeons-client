package com.kingx.dungeons;

public class BlockPair {

    private Block first;
    private Block second;
    private boolean removed = false;

    public BlockPair(Block first, Block second, boolean removed) {
        this.first = first;
        this.second = second;
        this.removed = removed;
    }

    public Block getFirst() {
        return first;
    }

    public void setFirst(Block first) {
        this.first = first;
    }

    public Block getSecond() {
        return second;
    }

    public void setSecond(Block second) {
        this.second = second;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}
