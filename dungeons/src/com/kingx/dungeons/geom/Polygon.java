package com.kingx.dungeons.geom;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;

public class Polygon {

    public static Mesh createPlain(float size) {
        float[] outVerts = new float[] { -size, -size, 0, size, -size, 0, size, size, 0, -size, size, 0 };
        short[] outIndices = new short[] { 1, 2, 0, 3 };
        Mesh poly = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position());
        poly.setVertices(outVerts);
        poly.setIndices(outIndices);
        return poly;
    }

    public static Mesh createPlain(float size, float u, float v, float u2, float v2) {
        float[] outVerts = new float[] { -size, -size, 0, u, v2, size, -size, 0, u2, v2, size, size, 0, u2, v, -size, size, 0, u, v };
        short[] outIndices = new short[] { 1, 2, 0, 3 };
        Mesh poly = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position(), VertexAttribute.TexCoords(0));
        poly.setVertices(outVerts);
        poly.setIndices(outIndices);
        return poly;
    }

}
