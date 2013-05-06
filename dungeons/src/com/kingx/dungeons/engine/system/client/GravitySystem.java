package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.AnimationComponent;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent.MovementType;
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
    @Mapper
    ComponentMapper<AnimationComponent> animationMapper;

    public GravitySystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        CollisionComponent collision = collisionMapper.get(e);

        if (position.isAnimation()) {
            return;
        } else if (position.getMovementType() == MovementType.CLIMB) {
            if (position.getY() < App.getTerrain().getHeight()) {
                if (animationMapper.has(e)) {
                    if (collision.getDown() != null) {
                        position.setMovementType(MovementType.WALK);
                        animationMapper.get(e).play("walk");
                    } else {
                        animationMapper.get(e).play("climb");
                    }
                }
                return;
            } else {
                position.setMovementType(MovementType.WALK);
                if (animationMapper.has(e)) {
                    animationMapper.get(e).play("walk");
                }
            }
        }

        GravityComponent gravity = gravityMapper.get(e);

        if (collision.getDown() != null) {
            position.setMovementType(MovementType.WALK);
            gravity.move.vector.y = 0;

            if (animationMapper.has(e)) {
                animationMapper.get(e).play("walk");
            }
        } else {
            if (animationMapper.has(e) && collision.getStandingOnABlock() == null) {
                animationMapper.get(e).play("jump");
                position.setMovementType(MovementType.JUMP);
            }
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
