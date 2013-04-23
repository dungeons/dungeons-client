package com.kingx.dungeons.graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.kingx.dungeons.Assets;

public class MeshFactory {
    public static Mesh build(String name) {

        FileHandle file = Gdx.files.internal("data/models/" + name + ".obj");
        Mesh mesh = null;
        if (file.exists()) {
            mesh = new ObjLoader().loadObj(file, true).getSubMeshes()[0].mesh;
        } else {
            file = Gdx.files.internal("data/models/" + name + ".g3dt");
            mesh = ModelLoaderRegistry.loadStillModel(file).getSubMeshes()[0].mesh;
        }
        fixUVs(mesh, Assets.getTexture(name, 0));
        //  move(mesh, 0, App.getTerrain().getHeight(), 0);
        return mesh;
    }

    private static void move(Mesh mesh, float x, float y, float z) {
        int offset = mesh.getVertexAttributes().findByUsage(VertexAttributes.Usage.Position).offset / BYTES_IN_FLOAT;
        int size = mesh.getVertexAttributes().vertexSize / BYTES_IN_FLOAT;

        FloatBuffer buffer = mesh.getVerticesBuffer();
        for (int i = 0; i < buffer.capacity(); i += size) {

            buffer.put(i + offset, buffer.get(i + offset) + x);
            buffer.put(i + offset + 1, buffer.get(i + offset + 1) + y);
            buffer.put(i + offset + 2, buffer.get(i + offset + 2) + z);
        }

    }

    private static int BYTES_IN_FLOAT = 4;

    private static void fixUVs(Mesh mesh, TextureRegion texture) {
        VertexAttribute attribute = mesh.getVertexAttributes().findByUsage(VertexAttributes.Usage.TextureCoordinates);
        if (attribute == null)
            return;
        int offset = attribute.offset / BYTES_IN_FLOAT;
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
}
