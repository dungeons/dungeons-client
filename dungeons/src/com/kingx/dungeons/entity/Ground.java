package com.kingx.dungeons.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.App;
import com.kingx.dungeons.entity.graphics.QuadTextureFrameBuffer;
import com.kingx.dungeons.entity.graphics.Shader;

public class Ground extends RenderableEntity {
    private Mesh poly;
    private ShaderProgram shadowGeneratorShader;
    private ShaderProgram shadowProjectShader;
    private QuadTextureFrameBuffer shadowMap;
    private Camera[] lightCams;
    private Texture depthMap;

    public Ground(float size) {
        super(0, 0, 0, size, 0);
    }

    @Override
    protected void initRender() {
        float[] outVerts = new float[] { -getHalfSize(), -getHalfSize(), 0, getHalfSize(), -getHalfSize(), 0, getHalfSize(), getHalfSize(), 0,
                -getHalfSize(), getHalfSize(), 0 };
        short[] outIndices = new short[] { 1, 2, 0, 3 };
        poly = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position());
        poly.setVertices(outVerts);
        poly.setIndices(outIndices);

        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowProjectShader = Shader.getShader("shadowproj");

        Wanderer wand = App.getWanderer();
        int lights = wand.getEyesCount();
        lightCams = new Camera[lights];
        for (int i = 0; i < lights; i++) {
            lightCams[i] = wand.getEyes(i);
        }
        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, 1024, 1024, true);

    }

    @Override
    protected void doRender(Camera cam) {
        // TODO cant directly reference one player entity, light will be generated for all of them

        generateShadowMap();
        depthMap.bind();

        shadowProjectShader.begin();
        shadowProjectShader.setUniformMatrix("ProjectionMatrix", cam.projection);
        shadowProjectShader.setUniformMatrix("ViewMatrix", cam.view);
        for (int i = 0; i < lightCams.length; i++) {
            shadowProjectShader.setUniformMatrix("LightSourceProjectionViewMatrix[" + i + "]", lightCams[i].combined);
        }
        shadowProjectShader.setUniformi("DepthMap", 0);
        shadowProjectShader.setUniformf("v_lightSpacePosition", lightCams[0].position);
        shadowProjectShader.setUniformf("color", 0.4f, 0.6f, 0.7f, 1f);

        poly.render(shadowProjectShader, GL20.GL_TRIANGLE_STRIP);
        shadowProjectShader.end();
    }

    private void generateShadowMap() {
        shadowMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        for (int i = 0; i < lightCams.length; i++) {
            lightCams[i].update();
            depthMap = generateShadowMap(lightCams[i]);
        }

        shadowMap.end();
    }

    private Texture generateShadowMap(Camera lightCam) {
        shadowMap.nextTexture();
        shadowGeneratorShader.begin();
        shadowGeneratorShader.setUniformMatrix("ProjectionMatrix", lightCam.projection);
        shadowGeneratorShader.setUniformMatrix("ViewMatrix", lightCam.view);
        App.getMaze().getPoly().render(shadowGeneratorShader, GL20.GL_TRIANGLES);
        shadowGeneratorShader.end();

        return shadowMap.getColorBufferTexture();
    }

    @Override
    protected void doUpdate(float delta) {
        // ground doesn't move
    }

    public Texture getCbt() {
        return depthMap;
    }

}
