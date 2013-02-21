package com.kingx.dungeons.entity;

import java.util.Random;

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
    private ShaderProgram groundShader;
    private ShaderProgram shadowGeneratorShader;
    private ShaderProgram shadowProjectShader;
    private QuadTextureFrameBuffer shadowMap;
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

        groundShader = Shader.getShader("ground");
        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowProjectShader = Shader.getShader("shadowproj");

        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, 1024, 1024, true);

    }

    private final Random r = new Random();

    @Override
    protected void doRender(Camera cam) {

        // Gdx.gl.glClearColor(0.4f, 0.6f, 0.7f, 1f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // No culling of back faces
        //Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        // No depth testing
        // Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        // Enable blending
        //     Gdx.gl.glEnable(GL20.GL_BLEND);
        //    Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
        // TODO cant directly reference one player entity, light will be generated for all of them

        // No culling of back faces
        for (CleverEntity ce : App.getUpdateList()) {
            //CleverEntity ce = App.getUpdateList().get(r.nextInt(App.getUpdateList().size()));
            if (ce instanceof ShadowCastingEntity) {
                ShadowCastingEntity sce = (ShadowCastingEntity) ce;
                Camera[] lights = sce.getLights();
                generateShadowMap(lights);

                System.out.println(sce);
                depthMap.bind();

                shadowProjectShader.begin();
                shadowProjectShader.setUniformMatrix("ProjectionMatrix", cam.projection);
                shadowProjectShader.setUniformMatrix("ViewMatrix", cam.view);
                for (int i = 0; i < lights.length; i++) {
                    shadowProjectShader.setUniformMatrix("LightSourceProjectionViewMatrix[" + i + "]", lights[i].combined);
                }
                shadowProjectShader.setUniformf("v_lightSpacePosition", lights[0].position);
                shadowProjectShader.setUniformi("DepthMap", 0);
                // shadowProjectShader.setUniformf("color", 0.4f, 0.6f, 0.7f, 1f);

                poly.render(shadowProjectShader, GL20.GL_TRIANGLE_STRIP);
                shadowProjectShader.end();

                break;
            }
        }
        //   Gdx.gl.glDisable(GL20.GL_BLEND);
        //}

        // No culling of back faces
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        // No depth testing
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        // Enable blending
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        groundShader.begin();
        groundShader.setUniformMatrix("PVMMatrix", cam.combined);
        groundShader.setUniformf("u_mazeSize", App.MAZE_BLOCKS_COUNT * App.MAZE_WALL_SIZE);
        poly.render(groundShader, GL20.GL_TRIANGLE_STRIP);
        groundShader.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void generateShadowMap(Camera[] lights) {
        shadowMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        for (int i = 0; i < lights.length; i++) {
            lights[i].update();
            depthMap = generateShadowMap(lights[i]);
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

    public Texture getCbt() {
        return depthMap;
    }

}
