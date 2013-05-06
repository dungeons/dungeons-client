package com.kingx.dungeons;

public class BlockPair {

    private Block first;
    private Block second;
    private boolean visible = true;

    public BlockPair(Block first, Block second, boolean visible) {
        this.first = first;
        this.second = second;
        this.visible = visible;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
