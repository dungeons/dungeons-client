package com.kingx.dungeons;

public class BlockPair {

    private Block first;
    private Block second;

    public BlockPair(Block first, Block second) {
        this.first = first;
        this.second = second;
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

}
