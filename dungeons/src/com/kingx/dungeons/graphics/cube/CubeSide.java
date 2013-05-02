package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;

public class CubeSide {
    public static final int INDICES = 6;
    public static final int VERTS = 4;
    private CubeVertex[] verts = new CubeVertex[VERTS];
    private boolean visible = true;
    private final CubeSideType type;

    public CubeSide(ArrayList<CubeVertex> quad, CubeSideType type) {
        this.verts = quad.toArray(verts);
        this.type = type;
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

    public CubeSideType getType() {
        return type;
    }

}
