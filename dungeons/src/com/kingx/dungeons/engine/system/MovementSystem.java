package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;

public class MovementSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SpeedComponent> speedMapper;
    @Mapper
    ComponentMapper<MoveComponent> inputMapper;
    @Mapper
    ComponentMapper<FollowCameraComponent> cameraMapper;

    public MovementSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, SpeedComponent.class, MoveComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        SpeedComponent speed = speedMapper.get(e);
        MoveComponent moveVector = inputMapper.get(e);

        position.x += moveVector.vector.x * speed.speed * world.delta;
        position.y += moveVector.vector.y * speed.speed * world.delta;

        if (cameraMapper.has(e)) {
            FollowCameraComponent cameraComponent = cameraMapper.get(e);
            cameraComponent.camera.position.x = position.x;
            cameraComponent.camera.position.y = position.y - 2f;
            cameraComponent.camera.lookAt(position.x, position.y, position.z);
            cameraComponent.camera.position.z = cameraComponent.height;
        }
    }
}
