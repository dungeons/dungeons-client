package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;

public class GravitySystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;
    @Mapper
    ComponentMapper<GravityComponent> gravityMapper;
    @Mapper
    ComponentMapper<CollisionComponent> collisionMapper;

    public GravitySystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        GravityComponent gravity = gravityMapper.get(e);
        CollisionComponent collision = collisionMapper.get(e);

        if (collision.getDown() != null) {
            gravity.setFalling(false);
            gravity.move.vector.y = 0;
        } else {
            gravity.setFalling(true);
            resolveMove(position, gravity);
        }
    }

    /**
     * Checks for collision on given position. If object is entity is colliding,
     * it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param mass
     *            size of entity
     */
    protected void resolveMove(PositionComponent position, GravityComponent gravity) {
        gravity.move.vector.y -= gravity.mass * this.world.getDelta();
    }

}
