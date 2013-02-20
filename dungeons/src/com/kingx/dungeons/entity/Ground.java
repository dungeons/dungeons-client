package com.kingx.dungeons.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.entity.graphics.Shader;

public class Ground extends RenderableEntity {
    private Mesh poly;
    private ShaderProgram shadowGeneratorShader;
    private ShaderProgram shadowProjectShader;
    private FrameBuffer shadowMap;
    private Camera[] lightCams;
    private Texture[] depthMaps;

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
        depthMaps = new Texture[lights];
        for (int i = 0; i < lights; i++) {
            lightCams[i] = wand.getEyes(i);
        }

    }

    @Override
    protected void doRender(Camera cam) {
        // TODO cant directly reference one player entity, light will be generated for all of them

        for (int i = 0; i < lightCams.length; i++) {
            lightCams[i].update();
            depthMaps[i] = generateShadowMap(lightCams[i]);
        }

        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        depthMaps[0].bind();
        if (lightCams.length > 1) {
            Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
            depthMaps[1].bind();
        }
        if (lightCams.length > 2) {
            Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE2);
            depthMaps[2].bind();
        }
        if (lightCams.length > 3) {
            Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE3);
            depthMaps[3].bind();
        }
        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);

        // Shadowmap gen
        shadowProjectShader.begin();
        shadowProjectShader.setUniformMatrix("ProjectionMatrix", cam.projection);
        shadowProjectShader.setUniformMatrix("ViewMatrix", cam.view);

        for (int i = 0; i < lightCams.length; i++) {
            shadowProjectShader.setUniformMatrix("LightSourceProjectionMatrix[" + i + "]", lightCams[i].projection);
            shadowProjectShader.setUniformMatrix("LightSourceViewMatrix[" + i + "]", lightCams[i].view);
            shadowProjectShader.setUniformi("DepthMap[" + i + "]", i);
        }
        
        shadowProjectShader.setUniformf("v_lightSpacePosition", lightCams[0].position);
        shadowProjectShader.setUniformf("color", 0.4f, 0.6f, 0.7f, 1f);

        poly.render(shadowProjectShader, GL20.GL_TRIANGLE_STRIP);
        shadowProjectShader.end();
    }

    private Texture generateShadowMap(Camera lightCam) {
        shadowMap = new FrameBuffer(Format.RGBA8888, 256, 256, true);
        shadowMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        shadowGeneratorShader.begin();
        shadowGeneratorShader.setUniformMatrix("ProjectionMatrix", lightCam.projection);
        shadowGeneratorShader.setUniformMatrix("ViewMatrix", lightCam.view);
        App.getMaze().poly.render(shadowGeneratorShader, GL20.GL_TRIANGLES);
        shadowGeneratorShader.end();
        shadowMap.end();

        return shadowMap.getColorBufferTexture();
    }

    @Override
    protected void doUpdate(float delta) {
        // TODO Auto-generated method stub
    }

    public Texture[] getCbt() {
        return depthMaps;
    }

}
