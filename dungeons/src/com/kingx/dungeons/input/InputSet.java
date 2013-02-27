package com.kingx.dungeons.input;

import com.badlogic.gdx.Input.Keys;

public enum InputSet {
    Player1(Keys.W, Keys.S, Keys.A, Keys.D);

    private int up;
    private int down;
    private int left;
    private int right;

    private InputSet(int up, int down, int left, int right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

}
