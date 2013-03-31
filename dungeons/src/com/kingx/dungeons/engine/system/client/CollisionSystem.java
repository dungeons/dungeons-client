package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public class CollisionSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;
    @Mapper
    ComponentMapper<CollisionComponent> collisionMapper;

    public CollisionSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, SizeComponent.class, CollisionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        SizeComponent size = sizeMapper.get(e);
        CollisionComponent collision = collisionMapper.get(e);

        collision.setUp(resolveCollisionUp(position, size));
        collision.setDown(resolveCollisionDown(position, size));
        collision.setLeft(resolveCollisionLeft(position, size));
        collision.setRight(resolveCollisionRight(position, size));
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
    protected Int resolveCollisionUp(PositionComponent position, SizeComponent size) {

        // FIXME Nasty hack: half-size is cut off so it does not collide on borders.
        float halfSize = size.getSize() / 2f - 0.1f;
        float x = position.getScreenX();
        float y = position.getY();

        float upBound = y + halfSize;

        Int upPoint = new Point.Int((int) (x / App.UNIT), (int) (upBound / App.UNIT));

        boolean collision = false;
        if (!Collision.isWalkable(upPoint)) {
            y = upPoint.y * App.UNIT - halfSize;
            collision = true;
        }

        position.setY(y);

        return collision ? upPoint : null;
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
    protected Int resolveCollisionDown(PositionComponent position, SizeComponent size) {

        // FIXME Nasty hack: half-size is cut off so it does not collide on borders.
        float halfSize = size.getSize() / 2f - 0.1f;
        float x = position.getScreenX();
        float y = position.getY();

        float downBound = y - halfSize;

        Int downPoint = new Point.Int((int) (x / App.UNIT), (int) (downBound / App.UNIT));

        boolean collision = false;
        if (!Collision.isWalkable(downPoint)) {
            y = (downPoint.y + 1) * App.UNIT + halfSize;
            collision = true;
        }

        position.setY(y);

        return collision ? downPoint : null;
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
    protected Int resolveCollisionLeft(PositionComponent position, SizeComponent size) {

        // FIXME Nasty hack: half-size is cut off so it does not collide on borders.
        float halfSize = size.getSize() / 2f - 0.1f;
        float x = position.getScreenX();
        float y = position.getY();

        float leftBound = x - halfSize;

        Int leftPoint = new Point.Int((int) (leftBound / App.UNIT), (int) (y / App.UNIT));

        boolean collision = false;
        if (!Collision.isWalkable(leftPoint)) {
            x = (leftPoint.x + 1) * App.UNIT + halfSize;
            collision = true;
        }

        position.setScreenX(x);

        return collision ? leftPoint : null;
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
    protected Int resolveCollisionRight(PositionComponent position, SizeComponent size) {

        // FIXME Nasty hack: half-size is cut off so it does not collide on borders.
        float halfSize = size.getSize() / 2f - 0.1f;
        float x = position.getScreenX();
        float y = position.getY();

        float rightBound = x + halfSize;

        Int rightPoint = new Point.Int((int) (rightBound / App.UNIT), (int) (y / App.UNIT));

        boolean collision = false;
        if (!Collision.isWalkable(rightPoint)) {
            x = rightPoint.x * App.UNIT - halfSize;
            collision = true;
        }

        position.setScreenX(x);

        return collision ? rightPoint : null;
    }

}
