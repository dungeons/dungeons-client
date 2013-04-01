package com.kingx.dungeons.engine.system.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Collision;

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
        SizeComponent size = sizeMapper.get(e);
        GravityComponent gravity = gravityMapper.get(e);
        CollisionComponent collision = collisionMapper.get(e);

        PositionComponent copy = new PositionComponent(position.inWorld);
        copy.inWorld.y -= 0.05f;

        if (Collision.resolveCollisionDown(copy, size) == null) {
            gravity.setFalling(true);
            resolveMove(position, gravity);
        } else if (collision.getUp() != null) {
            System.out.println("coliding up");
            if (gravity.move.vector.y > 0) {
                gravity.move.vector.y = 0;
                gravity.setFalling(false);
            }
        } else if (gravity.isFalling() && gravity.move.vector.y < 0) {
            System.out.println("resest.");
            gravity.setFalling(false);
            gravity.move.vector.y = Gdx.input.isKeyPressed(Keys.S) ? -1 : 0;
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

        System.out.println("resolve.");
        gravity.move.vector.y -= gravity.mass * this.world.getDelta();
    }

}
