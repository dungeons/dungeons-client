package com.kingx.dungeons.engine.system;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
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
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.geom.Point.Int;
import com.kingx.dungeons.graphics.QuadTextureFrameBuffer;
import com.kingx.dungeons.graphics.Shader;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.graphics.cube.CubeRenderer;

public class RenderShadowmapSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<MoveComponent> mm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final FollowCameraComponent camera;

    private final ShaderProgram shadowGeneratorShader;
    private final QuadTextureFrameBuffer shadowMap;
    private Texture depthMap;
    private CubeRenderer batchRender;
    private final ArrayList<CubeRegion> blocks;

    public RenderShadowmapSystem(FollowCameraComponent camera, ArrayList<CubeRegion> blocks) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShadowComponent.class));
        this.camera = camera;
        this.blocks = blocks;

        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, ShadowComponent.TEXTURE_SIZE, ShadowComponent.TEXTURE_SIZE, true);

    }

    @Override
    protected void begin() {
        if (batchRender == null) {
            batchRender = new CubeRenderer();
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

        Camera[] lights = sc.getLights();
        sc.move(pc);
        sc.rotate(mc);

        generateShadowMap(blocks, lights);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        depthMap.bind();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Assets.getTexture("terrain", 0).getTexture().bind();
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

        Int point = App.getPlayer().getCollision().getCurrent();
        batchRender.drawSubregion(cubeRegions.get(App.getCurrentView()), point.x, point.y, 4, true, false);
        batchRender.end();
        shadowGeneratorShader.end();
        return shadowMap.getColorBufferTexture();
    }

    public Texture getDepthMap() {
        return depthMap;
    }

}
