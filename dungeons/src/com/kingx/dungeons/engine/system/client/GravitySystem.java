package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public class GravitySystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;
    @Mapper
    ComponentMapper<GravityComponent> gravityMapper;

    public GravitySystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        GravityComponent gravity = gravityMapper.get(e);
        SizeComponent size = sizeMapper.get(e);

        gravity.setFalling(resolveFall(position, size));
        if (gravity.isFalling()) {
            resolveMove(position, gravity);
        } else {
            if (gravity.move.vector.y > 0) {
                gravity.move.vector.y = 0;
            }
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

    /**
     * Checks for collision on given position. If object is entity is colliding,
     * it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param size
     *            size of entity
     * @return {@code true} whether there were collision, {@code false}
     *         otherwise
     */
    protected boolean resolveFall(PositionComponent position, SizeComponent size) {

        float halfSize = size.getSize() / 2f;
        float x = position.getScreenX();
        float y = position.getY();

        // FIXME downBound is offset by 0.1f so it intersects. Its error prone and ugly.
        float downBound = y - halfSize - 0.1f;

        Int downPoint = new Point.Int((int) (x / App.UNIT), (int) (downBound / App.UNIT));

        return Collision.isWalkable(downPoint);
    }

}
