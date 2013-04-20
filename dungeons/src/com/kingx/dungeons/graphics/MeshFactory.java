package com.kingx.dungeons.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.kingx.dungeons.Assets;

public class MeshFactory {

    public static Mesh build(FileHandle fh) throws IOException {
        BufferedReader reader = fh.reader(1024);
        int status = 0;

        // Mesh params
        String name = null;
        Mesh mesh = null;
        short[] indices = null;
        VertexAttribute[] attributes = null;
        float[] vertices = null;

        while (true) {
            switch (status) {
                case 0:
                    name = getName(reader);
                    break;
                case 1:
                    indices = getIndices(reader);
                    break;
                case 2:
                    attributes = getAttributes(reader);
                    break;
                case 3:
                    vertices = getVerts(reader, attributes);
                    break;
                default:
                    System.out.println(Arrays.toString(indices));
                    System.out.println(Arrays.toString(vertices));
                    mesh = new Mesh(true, vertices.length, indices.length, attributes);
                    mesh.setVertices(vertices);
                    mesh.setIndices(indices);
                    return mesh;

            }
            status++;
        }
    }

    private static short[] getIndices(BufferedReader reader) throws IOException {
        String num = reader.readLine();
        int lines = Integer.valueOf(num);

        short[] indices = new short[lines * 3];
        StringBuffer data = new StringBuffer(lines * 6);
        for (int i = 0; i < lines; i++) {
            data.append(reader.readLine() + (i < lines - 1 ? "," : ""));
        }
        String[] stringIndices = data.toString().split(",");

        assert (stringIndices.length == indices.length);

        for (int i = 0; i < indices.length; i++) {
            indices[i] = Short.valueOf(stringIndices[i].trim());
        }

        return indices;
    }

    private static VertexAttribute[] getAttributes(BufferedReader reader) throws IOException {
        String num = reader.readLine();
        int lines = Integer.valueOf(num);

        VertexAttribute[] va = new VertexAttribute[lines];

        for (int i = 0; i < lines; i++) {
            va[i] = matchAttribute(reader.readLine().trim());
        }
        return va;
    }

    private enum VertexAttributeType {
        POSITION,
        NORMAL,
        COLOR,
        UV;
    }

    private static VertexAttribute matchAttribute(String name) {
        switch (VertexAttributeType.valueOf(name.toUpperCase())) {
            case POSITION:
                return VertexAttribute.Position();
            case NORMAL:
                return VertexAttribute.Normal();
            case COLOR:
                return VertexAttribute.ColorUnpacked();
            case UV:
                return VertexAttribute.TexCoords(0);
        }
        return null;
    }

    private static float[] getVerts(BufferedReader reader, VertexAttribute[] attributes) throws IOException {
        int vertSize = getAttributesSize(attributes);
        int lines = Integer.valueOf(reader.readLine());
        float[] verts = new float[lines * vertSize];
        StringBuffer data = new StringBuffer();
        for (int i = 0; i < lines; i++) {
            data.append(reader.readLine() + (i < lines - 1 ? "," : ""));
        }
        String[] stringVerts = data.toString().split(",");

        assert (stringVerts.length == verts.length);

        for (int i = 0; i < verts.length; i++) {
            verts[i] = Float.valueOf(stringVerts[i].trim());
        }

        return verts;
    }

    private static int getAttributesSize(VertexAttribute[] attributes) {
        int count = 0;
        for (VertexAttribute attrib : attributes) {
            count += attrib.numComponents;
        }
        return count;
    }

    private static String getName(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    public static Mesh build(String name) {
        Mesh mesh = new ObjLoader().loadObj(Gdx.files.internal("data/models/" + name + ".obj"), true).getSubMeshes()[0].mesh;
        fixUVs(mesh, Assets.getTexture(name, 0));
        return mesh;
    }

    private static int BYTES_IN_FLOAT = 4;

    private static void fixUVs(Mesh mesh, TextureRegion texture) {
        int offset = mesh.getVertexAttributes().findByUsage(VertexAttributes.Usage.TextureCoordinates).offset / BYTES_IN_FLOAT;
        int size = mesh.getVertexAttributes().vertexSize / BYTES_IN_FLOAT;

        FloatBuffer buffer = mesh.getVerticesBuffer();
        for (int i = 0; i < buffer.capacity(); i += size) {
            float x = buffer.get(i + offset);
            float y = buffer.get(i + offset + 1);

            x = x * (texture.getU2() - texture.getU()) + texture.getU();
            y = y * (texture.getV2() - texture.getV()) + texture.getV();

            buffer.put(i + offset, x);
            buffer.put(i + offset + 1, y);
        }

    }

    public static Mesh scale(Mesh mesh, float scale) {
        int offset = mesh.getVertexAttributes().findByUsage(VertexAttributes.Usage.Position).offset / BYTES_IN_FLOAT;
        int size = mesh.getVertexAttributes().vertexSize / BYTES_IN_FLOAT;

        FloatBuffer buffer = mesh.getVerticesBuffer();
        for (int i = 0; i < buffer.capacity(); i += size) {
            float x = buffer.get(i + offset + 0);
            float y = buffer.get(i + offset + 1);
            float z = buffer.get(i + offset + 2);

            x *= scale;
            y *= scale;
            z *= scale;

            buffer.put(i + offset + 0, x);
            buffer.put(i + offset + 1, y);
            buffer.put(i + offset + 2, z);
        }

        return mesh;

    }
}
