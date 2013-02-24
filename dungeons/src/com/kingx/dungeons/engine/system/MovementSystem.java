package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.kingx.dungeons.engine.component.InputComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;

public class MovementSystem extends EntitySystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SpeedComponent> speedMapper;
    @Mapper
    ComponentMapper<InputComponent> inputMapper;

    public MovementSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, SpeedComponent.class, InputComponent.class));
    }

    protected void process(Entity e) {
        System.out.println("MOVEMENT");
        PositionComponent position = postionMapper.get(e);
        SpeedComponent speed = speedMapper.get(e);
        InputComponent moveVector = inputMapper.get(e);

        position.x += moveVector.vector.x * speed.speed * world.delta;
        position.y += moveVector.vector.y * speed.speed * world.delta;
    }

    @Override
    protected final void processEntities(ImmutableBag<Entity> entities) {
        System.out.println(entities.size());
        for (int i = 0, s = entities.size(); s > i; i++) {
            process(entities.get(i));
        }
    }

    @Override
    protected boolean checkProcessing() {
        System.out.println("check");
        // TODO Auto-generated method stub
        return true;
    }

}
