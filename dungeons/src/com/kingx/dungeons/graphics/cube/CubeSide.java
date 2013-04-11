package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

public class CubeSide {
    public static final int INDICES = 6;
    public static final int VERTS = 4;
    private CubeVertex[] verts = new CubeVertex[VERTS];
    private boolean visible = true;

    public CubeSide(ArrayList<CubeVertex> quad) {
        this.verts = quad.toArray(verts);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public CubeVertex[] getVerts() {
        return verts;
    }

}
