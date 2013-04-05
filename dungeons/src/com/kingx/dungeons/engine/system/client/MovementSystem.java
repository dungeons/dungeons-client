package com.kingx.dungeons.engine.system.client;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
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
        MoveComponent move = inputMapper.get(e);

        Vector3 result = position.get().add(move.getRotatedVector().mul(speed.getCurrent() * world.delta));
        position.set(result);

    }
}
