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
import com.kingx.dungeons.geom.Point.Int;

public class CollisionSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;
    @Mapper
    ComponentMapper<GravityComponent> gravityMapper;

    public CollisionSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, SizeComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        SizeComponent size = sizeMapper.get(e);
        resolveMove(position, size);
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
    protected boolean resolveMove(PositionComponent position, SizeComponent size) {
        /*
                float halfSize = size.getSize() / 2f;
                float x = Collision.converX(position);
                float y = position.getY();

                float leftBound = x - halfSize;
                float rightBound = x + halfSize;
                float downBound = y - halfSize;
                float upBound = y + halfSize;

                Int leftPoint = new Point.Int((int) (leftBound / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));
                Int rightPoint = new Point.Int((int) (rightBound / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));
                Int downPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (downBound / App.MAZE_WALL_SIZE));
                Int upPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (upBound / App.MAZE_WALL_SIZE));

                boolean collision = false;
                if (!isWalkable(leftPoint)) {
                    x = (leftPoint.x + 1) * App.MAZE_WALL_SIZE + halfSize;
                } else if (!isWalkable(rightPoint)) {
                    x = rightPoint.x * App.MAZE_WALL_SIZE - halfSize;
                }

                if (!isWalkable(downPoint)) {
                    y = (downPoint.y + 1) * App.MAZE_WALL_SIZE + halfSize;
                    collision = true;
                } else if (!isWalkable(upPoint)) {
                    y = upPoint.y * App.MAZE_WALL_SIZE - halfSize;
                    collision = true;
                }*/

        //  position.setX(Collision.unconverX(x, position));
        // position.setY(y);

        return false;
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
