package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;

public abstract class CameraSystem extends EntityProcessingSystem {

    private final Camera camera;

    public CameraSystem(Camera camera, Aspect aspect) {
        super(aspect);
        this.camera = camera;
    }

    @Override
    protected void process(Entity e) {
        process(camera, e);
    }

    protected abstract void process(Camera camera, Entity e);

}
