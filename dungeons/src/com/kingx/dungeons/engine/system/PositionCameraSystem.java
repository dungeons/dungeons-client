package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.engine.component.PositionComponent;

public class PositionCameraSystem extends CameraSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;

    @SuppressWarnings("unchecked")
    public PositionCameraSystem(Camera camera) {
        super(camera, Aspect.getAspectForAll(PositionComponent.class));
    }

    @Override
    protected void process(Camera camera, Entity e) {
        PositionComponent position = postionMapper.get(e);
        camera.position.x = position.x;
        camera.position.y = position.y;
    }

}
