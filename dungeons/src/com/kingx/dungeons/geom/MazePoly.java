package com.kingx.dungeons.geom;

import com.badlogic.gdx.graphics.Mesh;

public class MazePoly {
    private final Mesh mesh;
    private final float[] convertedVerts;
    private final short[] indices;

    public MazePoly(Mesh mesh, float[] verts, short[] indices) {
        this.mesh = mesh;
        this.indices = indices;
        this.convertedVerts = convert(verts);

        this.mesh.setVertices(verts);
        this.mesh.setIndices(indices);
    }

    private float[] convert(float[] v) {
        int l = v.length / 8;
        int l2 = v.length / 8 * 3;
        float[] arr = new float[l2];
        for (int i = 0; i < l; i++) {
            arr[i * 3 + 0] = v[i * 8 + 0];
            arr[i * 3 + 1] = v[i * 8 + 1];
            arr[i * 3 + 2] = v[i * 8 + 2];
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
