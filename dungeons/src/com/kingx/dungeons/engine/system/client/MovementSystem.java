package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;

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
        postionMapper.has(e);
        PositionComponent position = postionMapper.get(e);
        RotationComponent rotation = rotationMapper.get(e);
        SpeedComponent speed = speedMapper.get(e);
        MoveComponent moveVector = inputMapper.get(e);

        position.vector.x += moveVector.getX() * speed.getCurrent() * world.delta;
        position.vector.y += moveVector.getY() * speed.getCurrent() * world.delta;

        if (cameraMapper.has(e)) {
            FollowCameraComponent cameraComponent = cameraMapper.get(e);
            cameraComponent.getCamera().position.x = position.getX();
            if (App.isFps()) {
                cameraComponent.getCamera().position.y = position.getY();
                cameraComponent.getCamera().position.z = 1f;
                cameraComponent.getCamera().direction.set(rotation.getVector());
                cameraComponent.getCamera().up.set(0, 0, 1);
            } else {
                cameraComponent.getCamera().position.y = position.getY() + 2f;
                cameraComponent.getCamera().lookAt(position.getX(), position.getY(), position.getZ());
                cameraComponent.getCamera().position.z = cameraComponent.getHeight();
            }
        }
    }
}
