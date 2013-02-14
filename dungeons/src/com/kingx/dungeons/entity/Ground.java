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
import com.kingx.dungeons.entity.graphics.Shader;

public class Ground extends RenderableEntity {
    private Mesh poly;
    private ShaderProgram shadowGeneratorShader;
    private ShaderProgram shadowProjectShader;
    private FrameBuffer shadowMap;
    private Camera lightCam;

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

        shadowMap = new FrameBuffer(Format.RGBA8888, 1024, 1024, true);

    }

    private float angle = 1;
    private Texture cbt;

    @Override
    protected void doRender(Camera cam) {
        // TODO cant directly reference one player entity, light will be generated for all of them
        lightCam = App.getWanderer().getEyes().getCamera();
        System.out.println(lightCam.position.x + " x");
        System.out.println(lightCam.position.y + " y");
        System.out.println(lightCam.position.z + " z");

        // lightCam = new PerspectiveCamera(67, shadowMap.getWidth(), shadowMap.getHeight());
        // lightCam.position.set(15, 15, 5);
        // lightCam.lookAt(0, 0, 0);
        lightCam.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        // Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        // lightCam.combined.rotate(new Vector3(0,0,1), angle);
        shadowMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
      //  Gdx.gl.glCullFace(GL20.GL_FRONT);
        
        
        shadowGeneratorShader.begin();
        shadowGeneratorShader.setUniformMatrix("ProjectionMatrix", lightCam.projection);
        shadowGeneratorShader.setUniformMatrix("ViewMatrix", lightCam.view);
        //poly.render(shadowGeneratorShader, GL20.GL_TRIANGLE_STRIP);
        App.getMaze().poly.render(shadowGeneratorShader, GL20.GL_TRIANGLES);
        shadowGeneratorShader.end();
        shadowMap.end();
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        shadowProjectShader.begin();
        cbt = shadowMap.getColorBufferTexture();
        shadowMap.getColorBufferTexture().bind();
        shadowProjectShader.setUniformi("s_shadowMap", 0);
        shadowProjectShader.setUniformMatrix("u_projTrans", cam.combined);
        shadowProjectShader.setUniformMatrix("u_lightProjTrans", lightCam.combined);
        shadowProjectShader.setUniformf("u_color", 0.8f, 0.5f, 0.2f, 1f);
        poly.render(shadowProjectShader, GL20.GL_TRIANGLE_STRIP);
        shadowProjectShader.end();


       

    }
    

    @Override
    protected void doUpdate(float delta) {
        // TODO Auto-generated method stub

    }

    public Texture getCbt() {
        return cbt;
    }

}
