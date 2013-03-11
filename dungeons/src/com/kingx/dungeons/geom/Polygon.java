package com.kingx.dungeons.geom;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;

public class Polygon {

    public static Mesh createPlain(float size) {
        float[] outVerts = new float[] { -size, -size, -1f, size, -size, -1f, size, size, -1f, -size, size, -1f };
        short[] outIndices = new short[] { 1, 2, 0, 3 };
        Mesh poly = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position());
        poly.setVertices(outVerts);
        poly.setIndices(outIndices);
        return poly;
    }

}
