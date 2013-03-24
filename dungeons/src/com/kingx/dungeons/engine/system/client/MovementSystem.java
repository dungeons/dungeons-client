package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
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

    public MovementSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, RotationComponent.class, SpeedComponent.class, MoveComponent.class));
    }

    @Override
    protected void process(Entity e) {
        postionMapper.has(e);
        PositionComponent position = postionMapper.get(e);
        SpeedComponent speed = speedMapper.get(e);
        MoveComponent moveVector = inputMapper.get(e);

        float unconverted = moveVector.getX() * speed.getCurrent() * world.delta;
        switch (App.getCurrentView()) {
            case 0:
                position.vector.x += unconverted;
                break;
            case 1:
                position.vector.x += unconverted;
                break;
            case 2:
                position.vector.x += unconverted;
                break;
            case 3:
                position.vector.z += unconverted;
                break;
        }
        position.vector.y += moveVector.getY() * speed.getCurrent() * world.delta;

    }
}
