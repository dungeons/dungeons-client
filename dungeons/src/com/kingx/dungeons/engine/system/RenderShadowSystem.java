package com.kingx.dungeons.engine.system;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.CubeRegion;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.geom.GroundFactory;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.CubeBatch;
import com.kingx.dungeons.graphics.QuadTextureFrameBuffer;
import com.kingx.dungeons.graphics.Shader;

public class RenderShadowSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final FollowCameraComponent camera;

    private boolean begin;
    private final ShaderProgram shadowGeneratorShader;
    private final ShaderProgram shadowProjectShader;
    private final QuadTextureFrameBuffer shadowMap;
    private final Mesh poly;
    private Texture depthMap;
    private CubeBatch batchRender;
    private ShaderProgram spriteShader;
    private CubeBatch batchRender2;

    private static final boolean experimental = true;
    private static final int TEXTURE_SIZE = 1024;

    public RenderShadowSystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShadowComponent.class));
        this.camera = camera;

        poly = new GroundFactory(App.getMap(), App.MAZE_WALL_SIZE).generate();

        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowProjectShader = Shader.getShader("shadowproj");

        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, TEXTURE_SIZE, TEXTURE_SIZE, true);

    }

    @Override
    protected void begin() {
        begin = true;
        if (batchRender == null) {
            batchRender = new CubeBatch();
            batchRender2 = new CubeBatch();
        }
    }

    @Override
    protected void end() {
        begin = false;
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.getSafe(e);
        ShadowComponent sc = sm.getSafe(e);

        ArrayList<CubeRegion> cubeRegions = App.getCubeRegions();

        Camera[] lights = sc.getLights();
        sc.update(pc);

        generateShadowMap(cubeRegions, lights);

        depthMap.bind();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        Assets.getTexture("wall", 0).getTexture().bind();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        batchRender.setShader(shadowProjectShader);
        batchRender.begin();
        shadowProjectShader.setUniformMatrix("ProjectionMatrix", camera.getCamera().projection);
        shadowProjectShader.setUniformMatrix("ViewMatrix", camera.getCamera().view);
        for (int i = 0; i < lights.length; i++) {
            shadowProjectShader.setUniformMatrix("LightSourceProjectionViewMatrix[" + i + "]", lights[i].combined);
        }
        shadowProjectShader.setUniformf("v_lightSpacePosition", lights[0].position);
        shadowProjectShader.setUniformi("DepthMap", 0);

        shadowProjectShader.setUniformf("u_source_color", Colors.WALL_LIGHT);
        shadowProjectShader.setUniformf("u_ground_color", Colors.WALL_SHADOW);
        shadowProjectShader.setUniformi("u_texture", 1);
        shadowProjectShader.setUniformf("u_useTextures", 1);
        shadowProjectShader.setUniformf("u_sight", App.getPlayer().getEntity().getComponent(SightComponent.class).getRadius());
        poly.render(shadowProjectShader, GL20.GL_TRIANGLES);
        batchRender.end();

        batchRender2.setShader(shadowProjectShader);
        batchRender2.begin();
        shadowProjectShader.setUniformf("u_source_color", Colors.WALL_LIGHT);
        shadowProjectShader.setUniformf("u_ground_color", Colors.WALL_SHADOW);

        batchRender2.draw(cubeRegions);
        batchRender2.end();

    }

    /**
     * Generates a depthmap for each light
     * 
     * @param lights
     *            lights for depthmap generation
     */
    private void generateShadowMap(ArrayList<CubeRegion> cubeRegions, Camera[] lights) {
        shadowMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        for (int i = 0; i < lights.length; i++) {
            lights[i].update();
            depthMap = generateShadowMap(cubeRegions, lights[i]);
        }

        shadowMap.end();
    }

    private Texture generateShadowMap(ArrayList<CubeRegion> cubeRegions, Camera lightCam) {
        shadowMap.nextTexture();
        shadowGeneratorShader.begin();

        shadowGeneratorShader.setUniformMatrix("ProjectionMatrix", lightCam.projection);
        shadowGeneratorShader.setUniformMatrix("ViewMatrix", lightCam.view);

        batchRender.setShader(shadowGeneratorShader);
        batchRender.begin();
        batchRender.draw(cubeRegions.get(App.getCurrentView()));
        batchRender.end();
        shadowGeneratorShader.end();
        return shadowMap.getColorBufferTexture();
    }

    public Texture getDepthMap() {
        return depthMap;
    }

}
