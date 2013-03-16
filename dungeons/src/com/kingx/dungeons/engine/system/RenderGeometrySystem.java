package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.dynamic.PositionComponent;
import com.kingx.dungeons.engine.dynamic.SizeComponent;
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
        if (sc.texture != null) {
            shader.setUniformf("u_tint", sc.color);
            PositionComponent pc = pm.getSafe(e);
            sb.draw(sc.texture, pc.vector.x - ccs.size / 2f, pc.vector.y - ccs.size / 2f, ccs.size, ccs.size);
        }
    }
}
