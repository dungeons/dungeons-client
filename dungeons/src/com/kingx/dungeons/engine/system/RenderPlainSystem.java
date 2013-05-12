package com.kingx.dungeons.engine.system;

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

public class RenderPlainSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<MoveComponent> mm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final FollowCameraComponent camera;

    private final ShaderProgram plainShader;
    private CubeRenderer batchRender;
    private final CubeRegion blocks;

    public RenderPlainSystem(FollowCameraComponent camera, CubeRegion blocks) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShadowComponent.class));
        this.camera = camera;
        this.blocks = blocks;

        plainShader = Shader.getShader("plain");

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
        if (pc.getY() > App.getTerrain().getHeight()) {
            MoveComponent mc = mm.getSafe(e);
            ShadowComponent sc = sm.getSafe(e);

            Camera[] lights = sc.getLights();
            sc.move(pc);
            sc.rotate(mc);

            batchRender.setShader(plainShader);
            batchRender.begin();
            plainShader.setUniformMatrix("ProjectionMatrix", camera.getCamera().projection);
            plainShader.setUniformMatrix("ViewMatrix", camera.getCamera().view);
            plainShader.setUniformf("v_lightSpacePosition", lights[0].position);

            plainShader.setUniformf("u_source_color", Colors.WALL_LIGHT);
            plainShader.setUniformf("u_ground_color", Colors.WALL_SHADOW);
            plainShader.setUniformi("u_texture", 0);
            plainShader.setUniformf("u_sight", App.getPlayer().getEntity().getComponent(SightComponent.class).getRadius());
            plainShader.setUniformf("u_side", App.getCurrentView());
            plainShader.setUniformf("u_tint", Colors.interpolate(Colors.SHADOW_BOTTOM, Color.WHITE, App.getProgress(), 1));

            batchRender.draw(blocks, false);
            batchRender.end();
        }
    }

}
