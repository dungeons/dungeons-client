package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
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

            float angle = cameraComponent.getAngle();
            cameraComponent.getCamera().position.x = 5 + (float) (Math.sin(angle) * cameraComponent.getHeight());
            cameraComponent.getCamera().position.y = position.getY();
            cameraComponent.getCamera().position.z = -5 + (float) (Math.cos(angle) * cameraComponent.getHeight());
            cameraComponent.getCamera().lookAt(5, position.getY(), -5);
        }

    }
}
