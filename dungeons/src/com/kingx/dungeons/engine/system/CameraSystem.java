package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Camera;

public abstract class CameraSystem extends EntitySystem {

    private final Camera camera;

    public CameraSystem(Camera camera, Aspect aspect) {
        super(aspect);
        this.camera = camera;
    }

    protected void process(Entity e) {
        process(camera, e);
    }

    protected abstract void process(Camera camera, Entity e);

    @Override
    protected final void processEntities(ImmutableBag<Entity> entities) {
        System.out.println(entities.size());
        for (int i = 0, s = entities.size(); s > i; i++) {
            process(entities.get(i));
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

}
