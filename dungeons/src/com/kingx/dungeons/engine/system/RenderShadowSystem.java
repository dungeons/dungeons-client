package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.geom.Polygon;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.QuadTextureFrameBuffer;
import com.kingx.dungeons.graphics.Shader;

public class RenderShadowSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final Camera camera;

    private boolean begin;
    private final ShaderProgram shadowGeneratorShader;
    private final ShaderProgram shadowProjectShader;
    private final QuadTextureFrameBuffer shadowMap;
    private final Mesh poly;
    private final float BOUNDS = 500f;
    private Texture depthMap;
    private static final int TEXTURE_SIZE = 1024;

    public RenderShadowSystem(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShadowComponent.class));
        this.camera = camera;

        poly = Polygon.createPlain(BOUNDS);

        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowProjectShader = Shader.getShader("shadowproj");

        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, TEXTURE_SIZE, TEXTURE_SIZE, true);

    }

    @Override
    protected void begin() {
        begin = true;
    }

    @Override
    protected void end() {
        begin = false;
    }

    @Override
    protected void process(Entity e) {
        if (begin) {
            PositionComponent pc = pm.getSafe(e);
            ShadowComponent sc = sm.getSafe(e);

            Camera[] lights = sc.getLights();
            for (Camera light : lights) {
                light.position.x = pc.getX();
                light.position.y = pc.getY();
            }
            generateShadowMap(lights);

            depthMap.bind();
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
            Assets.getTexture("wall", 0).getTexture().bind();
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

            shadowProjectShader.begin();
            shadowProjectShader.setUniformMatrix("ProjectionMatrix", camera.projection);
            shadowProjectShader.setUniformMatrix("ViewMatrix", camera.view);
            for (int i = 0; i < lights.length; i++) {
                shadowProjectShader.setUniformMatrix("LightSourceProjectionViewMatrix[" + i + "]", lights[i].combined);
            }
            shadowProjectShader.setUniformf("v_lightSpacePosition", lights[0].position);
            shadowProjectShader.setUniformf("u_source_color", Colors.GROUND);
            shadowProjectShader.setUniformf("u_ground_color", Colors.SHADOW);
            shadowProjectShader.setUniformi("DepthMap", 0);
            shadowProjectShader.setUniformf("u_useTextures", 0);
            poly.render(shadowProjectShader, GL20.GL_TRIANGLE_STRIP);
            shadowProjectShader.setUniformf("u_source_color", Colors.WALL_LIGHT);
            shadowProjectShader.setUniformf("u_ground_color", Colors.WALL_SHADOW);
            shadowProjectShader.setUniformi("u_texture", 1);
            shadowProjectShader.setUniformf("u_useTextures", 1);
            App.getMaze().getMesh().render(shadowProjectShader, GL20.GL_TRIANGLES);
            shadowProjectShader.end();
        }

    }

    /**
     * Generates a depthmap for each light
     * 
     * @param lights
     *            lights for depthmap generation
     */
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
        App.getMaze().getMesh().render(shadowGeneratorShader, GL20.GL_TRIANGLES);
        shadowGeneratorShader.end();
        return shadowMap.getColorBufferTexture();
    }

    public Texture getDepthMap() {
        return depthMap;
    }

}
