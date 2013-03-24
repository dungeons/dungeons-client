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
        if (false) {
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
        float x = position.getX();
        float y = position.getY();

        // FIXME downBound is offset by 0.1f so it intersects. Its error prone and ugly.
        float downBound = y - halfSize - 0.1f;

        Int downPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (downBound / App.MAZE_WALL_SIZE));

        return isWalkable(downPoint);
    }

    /**
     * Check whether is point in map bounds and walkable
     * 
     * @param point
     *            to be checked
     * @return {@code true} if point is walkable, {@code false} otherwise
     */
    private boolean isWalkable(Int point) {
        boolean[][] footprint = App.getMap().getFootprint();
        if (point.x < 0 || point.x >= footprint.length) {
            return false;
        }

        if (point.y < 0 || point.y >= footprint[0].length) {
            return false;
        }

        return footprint[point.x][point.y];
    }
}
