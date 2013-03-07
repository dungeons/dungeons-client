package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.RotationComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;

public class MovementSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<RotationComponent> rotationMapper;
    @Mapper
    ComponentMapper<SpeedComponent> speedMapper;
    @Mapper
    ComponentMapper<MoveComponent> inputMapper;
    @Mapper
    ComponentMapper<FollowCameraComponent> cameraMapper;

    public MovementSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, RotationComponent.class, SpeedComponent.class, MoveComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        RotationComponent rotation = rotationMapper.get(e);
        SpeedComponent speed = speedMapper.get(e);
        MoveComponent moveVector = inputMapper.get(e);

        position.vector.x += moveVector.vector.x * speed.speed * world.delta;
        position.vector.y += moveVector.vector.y * speed.speed * world.delta;

        if (cameraMapper.has(e)) {
            FollowCameraComponent cameraComponent = cameraMapper.get(e);
            cameraComponent.camera.position.x = position.vector.x;
            if (App.isFps()) {
                cameraComponent.camera.position.y = position.vector.y;
                cameraComponent.camera.position.z = 1f;
                cameraComponent.camera.direction.set(rotation.vector);
                cameraComponent.camera.up.set(0, 0, 1);
            } else {
                cameraComponent.camera.position.y = position.vector.y - 2f;
                cameraComponent.camera.lookAt(position.vector.x, position.vector.y, position.vector.z);
                cameraComponent.camera.position.z = cameraComponent.height;
            }
        }
    }
}
