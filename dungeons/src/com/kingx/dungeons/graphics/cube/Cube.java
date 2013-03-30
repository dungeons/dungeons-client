package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;


public class Cube {

    public static final int VERTS_PER_QUAD = 4;
    public static final int INDICES_PER_QUAD = 6;
    public static final int QUADS = 6;

    private final ArrayList<CubeVertex> verts;
    private CubeVertex[] vertsArray;

    public Cube() {
        this.verts = new ArrayList<CubeVertex>();
    }

    public CubeVertex[] getVerts() {
        return vertsArray;
    }

    public void addVerts(ArrayList<CubeVertex> quad) {
        verts.addAll(quad);
    }

    public void make() {
        vertsArray = verts.toArray(new CubeVertex[verts.size()]);
    }

}
