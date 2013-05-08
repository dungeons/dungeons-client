package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
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
        computeBoundaries();
    }

    private static final float NINE_OFFSET = 0f;

    public Vector3[] getNinePoints() {
        Vector3[] nine = new Vector3[9];

        CubeVertex[] vertexes = getVerts();
        for (int i = 0; i < vertexes.length; i++) {
            float[] pos = vertexes[i].getPosition();
            nine[i] = new Vector3(pos[0], pos[1], pos[2]);
        }

        for (int i = 0; i < vertexes.length; i++) {
            Vector3 prev = nine[i];
            Vector3 next = nine[(i + 1) % vertexes.length];
            nine[vertexes.length + i] = next.cpy().sub(prev).mul(0.5f).add(prev);
        }

        nine[nine.length - 1] = mean;

        //  System.out.println("INIT " + Arrays.toString(nine));
        if (NINE_OFFSET != 0) {
            Vector3 origin = mean.cpy();
            for (int i = 0; i < nine.length; i++) {
                nine[i].sub(origin);
                nine[i].mul(1 - NINE_OFFSET);
                nine[i].add(origin);
            }
        }

        //  System.out.println("     " + Arrays.toString(nine));
        return nine;
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

    public Vector3 min = new Vector3();
    public Vector3 mean = new Vector3();
    public Vector3 max = new Vector3();
    public Vector3 center = new Vector3();

    public void computeBoundaries() {
        min.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        max.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);

        for (CubeVertex vert : verts) {

            // x coord
            if (vert.getPosition()[0] < min.x) {
                min.x = vert.getPosition()[0];
            } else if (vert.getPosition()[0] > max.x) {
                max.x = vert.getPosition()[0];
            }

            // y coord
            if (vert.getPosition()[1] < min.y) {
                min.y = vert.getPosition()[1];
            } else if (vert.getPosition()[1] > max.y) {
                max.y = vert.getPosition()[1];
            }

            // z coord
            if (vert.getPosition()[2] < min.z) {
                min.z = vert.getPosition()[2];
            } else if (vert.getPosition()[2] > max.z) {
                max.z = vert.getPosition()[2];
            }
        }

        mean.set((max.x + min.x) / 2f, (max.y + min.y) / 2f, (max.z + min.z) / 2f);
        center.set(min.x + mean.x, min.y + mean.y, min.z + mean.z);
    }
}
