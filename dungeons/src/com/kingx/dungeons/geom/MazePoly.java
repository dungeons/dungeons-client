package com.kingx.dungeons.geom;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;

public class MazePoly {
    private final Mesh mesh;
    private final float[] convertedVerts;
    private final short[] indices;

    public MazePoly(Mesh mesh, float[] verts, short[] indices) {
        this.mesh = mesh;
        this.indices = indices;
        this.convertedVerts = filterPositionAttributes(verts);

        this.mesh.setVertices(verts);
        this.mesh.setIndices(indices);
    }

    private static final int BYTES_PER_FLOAT = 4;

    private float[] filterPositionAttributes(float[] v) {
        int currentVertexSize = mesh.getVertexSize() / BYTES_PER_FLOAT;
        int filteredVertexSize = VertexAttribute.Position().numComponents;

        int vertsCount = v.length / currentVertexSize;
        int size = vertsCount * filteredVertexSize;
        float[] arr = new float[size];
        for (int i = 0; i < vertsCount; i++) {
            for (int j = 0; j < filteredVertexSize; j++) {
                arr[i * filteredVertexSize + j] = v[i * currentVertexSize + j];
            }
        }
        return arr;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public float[] getVerts() {
        return convertedVerts;
    }

    public short[] getIndices() {
        return indices;
    }

    @Override
    public String toString() {
        return "MazePoly [mesh=" + mesh + ", verts=" + convertedVerts.length + ", indices=" + indices.length + "]";
    }

}
