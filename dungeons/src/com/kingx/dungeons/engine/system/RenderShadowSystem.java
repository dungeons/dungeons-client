package com.kingx.dungeons.engine.system;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
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
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.GroundFactory;
import com.kingx.dungeons.graphics.QuadTextureFrameBuffer;
import com.kingx.dungeons.graphics.Shader;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.graphics.cube.CubeRenderer;

public class RenderShadowSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<MoveComponent> mm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final FollowCameraComponent camera;

    private final ShaderProgram shadowGeneratorShader;
    private final ShaderProgram shadowProjectShader;
    private final QuadTextureFrameBuffer shadowMap;
    private final Mesh poly;
    private Texture depthMap;
    private CubeRenderer batchRender;
    private CubeRenderer batchRender2;

    private static final int TEXTURE_SIZE = 1024;

    public RenderShadowSystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShadowComponent.class));
        this.camera = camera;

        poly = new GroundFactory(App.getMap(), App.UNIT).generate();

        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowProjectShader = Shader.getShader("shadowproj");

        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, TEXTURE_SIZE, TEXTURE_SIZE, true);

    }

    @Override
    protected void begin() {
        if (batchRender == null) {
            batchRender = new CubeRenderer();
            batchRender2 = new CubeRenderer();
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.getSafe(e);
        MoveComponent mc = mm.getSafe(e);
        ShadowComponent sc = sm.getSafe(e);

        ArrayList<CubeRegion> cubeRegions = App.getCubeManager().getCubeRegions();

        Camera[] lights = sc.getLights();
        sc.move(pc);
        sc.rotate(mc);

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
        shadowProjectShader.setUniformf("u_side", App.getCurrentView());
        shadowProjectShader.setUniformf("u_worldWidth", CubeRegion.getWidth());
        shadowProjectShader.setUniformf("u_minBound", CubeRegion.min);
        shadowProjectShader.setUniformf("u_maxBound", CubeRegion.max);
        shadowProjectShader.setUniformf("u_tint", Colors.interpolate(Colors.SHADOW_BOTTOM, Color.WHITE, App.getProgress(), 1));
        poly.render(shadowProjectShader, GL20.GL_TRIANGLES);
        batchRender.end();

        batchRender2.setShader(shadowProjectShader);
        batchRender2.begin();
        shadowProjectShader.setUniformf("u_source_color", Colors.WALL_LIGHT);
        shadowProjectShader.setUniformf("u_ground_color", Colors.WALL_SHADOW);

        batchRender2.draw(cubeRegions.get(App.getLastView()));
        batchRender2.draw(cubeRegions.get(App.getCurrentView()));
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
