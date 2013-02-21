package engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

import engine.component.PositionComponent;
import engine.component.Velocity;

public class MovementSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<Velocity> vm;

    public MovementSystem() {
        super(Aspect.getAspectFor(PositionComponent.class, Velocity.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = pm.get(e);
        Velocity velocity = vm.get(e);

        position.x += velocity.vectorX * world.delta;
        position.y += velocity.vectorY * world.delta;
    }

}
