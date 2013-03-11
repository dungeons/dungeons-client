package com.kingx.dungeons.geom;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;

public class Polygon {

    private static final float OFFSET = .01f;

    public static Mesh createPlain(float size) {
        float[] outVerts = new float[] { -size, -size, -OFFSET, size, -size, -OFFSET, size, size, -OFFSET, -size, size, -OFFSET };
        short[] outIndices = new short[] { 1, 2, 0, 3 };
        Mesh poly = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position());
        poly.setVertices(outVerts);
        poly.setIndices(outIndices);
        return poly;
    }

}
