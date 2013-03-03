package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.tags.GeometryRenderTag;

public class RenderGeometrySystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<ShaderComponent> sm;
    @Mapper
    ComponentMapper<MeshComponent> mm;

    private final Camera camera;

    public RenderGeometrySystem(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShaderComponent.class, MeshComponent.class, GeometryRenderTag.class));
        this.camera = camera;
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.getSafe(e);
        ShaderComponent sc = sm.getSafe(e);
        MeshComponent mc = mm.getSafe(e);

        camera.combined.translate(pc.x, pc.y, pc.z);
        sc.shader.begin();
        sc.shader.setUniformMatrix("u_MVPMatrix", camera.combined);
        sc.shader.setUniformf("u_color", sc.color);
        mc.mesh.render(sc.shader, GL10.GL_TRIANGLES);
        sc.shader.end();

        camera.combined.translate(-pc.x, -pc.y, -pc.z);
    }

}
