package com.kingx.dungeons.engine.system;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.graphics.cube.CubeRenderer;

public class RenderMineralSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<MoveComponent> mm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final FollowCameraComponent camera;

    private final ShaderProgram shadowProjectShader;
    private CubeRenderer batchRender;
    private final ArrayList<CubeRegion> blocks;

    public RenderMineralSystem(FollowCameraComponent camera, ArrayList<CubeRegion> blocks) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShadowComponent.class));
        this.camera = camera;
        this.blocks = blocks;

        shadowProjectShader = Shader.getShader("shadowproj");

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

        batchRender.setShader(shadowProjectShader);
        batchRender.begin();
        shadowProjectShader.setUniformMatrix("ProjectionMatrix", camera.getCamera().projection);
        shadowProjectShader.setUniformMatrix("ViewMatrix", camera.getCamera().view);
        for (int i = 0; i < lights.length; i++) {
            shadowProjectShader.setUniformMatrix("LightSourceProjectionViewMatrix[" + i + "]", lights[i].combined);
        }
        shadowProjectShader.setUniformf("v_lightSpacePosition", lights[0].position);

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

        batchRender.enableBlending();
        batchRender.draw(blocks.get(App.getPrevView()), false);
        batchRender.draw(blocks.get(App.getCurrentView()), false);
        batchRender.draw(blocks.get(App.getNextView()), false);

        batchRender.end();

    }

}
