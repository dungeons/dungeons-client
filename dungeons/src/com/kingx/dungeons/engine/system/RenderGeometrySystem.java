package com.kingx.dungeons.engine.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.engine.tags.GeometryRenderTag;
import com.kingx.dungeons.graphics.Shader;

public class RenderGeometrySystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<ShaderComponent> sm;
    @Mapper
    ComponentMapper<SizeComponent> ss;

    private final Camera camera;
    private final ShaderProgram shader;
    private final SpriteBatch sb = new SpriteBatch();

    public RenderGeometrySystem(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShaderComponent.class, GeometryRenderTag.class));
        this.camera = camera;
        this.shader = Shader.getShader("sprite");
        sb.setShader(shader);
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        sb.begin();
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
        sb.end();
    }

    @Override
    protected void process(Entity e) {
        sb.setProjectionMatrix(camera.combined);
        ShaderComponent sc = sm.getSafe(e);
        SizeComponent ccs = ss.getSafe(e);
        if (sc.getTexture() != null) {
            shader.setUniformf("u_tint", sc.getColor());
            PositionComponent pc = pm.getSafe(e);
            sb.draw(sc.getTexture(), pc.getX() - ccs.getSize() / 2f, pc.getY() - ccs.getSize() / 2f, ccs.getSize(), ccs.getSize());
        }
    }
}
