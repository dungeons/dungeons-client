package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

public class Cube {

    public static final int QUADS = 6;

    private final CubeSide[] sides;
    private int position = 0;

    public Cube() {
        this.sides = new CubeSide[QUADS];
    }

    public CubeSide[] getSides() {
        return sides;
    }

    public void addVerts(ArrayList<CubeVertex> quad) {
        sides[position++] = new CubeSide(quad);
    }

    public void hide(int i) {
        sides[i].setVisible(false);
    }

}
