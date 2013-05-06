package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;

public class SimpleCube {

    public static final int QUADS = 6;

    private final CubeSide[] sides;
    private int position = 0;

    SimpleCube() {
        this.sides = new CubeSide[QUADS];
    }

    public CubeSide[] getSides() {
        return sides;
    }

    public void addVerts(ArrayList<CubeVertex> quad, CubeSideType type) {
        sides[position++] = new CubeSide(quad, type);
    }

}
