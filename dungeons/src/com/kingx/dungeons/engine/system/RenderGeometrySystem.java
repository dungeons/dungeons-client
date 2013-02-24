package com.kingx.dungeons.engine.system;

import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;

public class RenderGeometrySystem extends EntitySystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<ShaderComponent> sm;
    @Mapper
    ComponentMapper<MeshComponent> mm;

    private final Camera camera;

    private Bag<AtlasRegion> regionsByEntity;
    private List<Entity> sortedEntities;

    public RenderGeometrySystem(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShaderComponent.class, MeshComponent.class));
        this.camera = camera;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        for (int i = 0; entities.size() > i; i++) {
            process(entities.get(i));
        }
    }

    protected void process(Entity e) {
        PositionComponent pc = pm.getSafe(e);
        System.out.println(pc);
        ShaderComponent sc = sm.getSafe(e);
        MeshComponent mc = mm.getSafe(e);

        camera.combined.translate(pc.x, pc.y, pc.z);

        sc.shader.begin();
        sc.shader.setUniformMatrix("u_MVPMatrix", camera.combined);
        mc.mesh.render(sc.shader, GL10.GL_TRIANGLES);
        sc.shader.end();

        camera.combined.translate(-pc.x, -pc.y, -pc.z);
    }

}
