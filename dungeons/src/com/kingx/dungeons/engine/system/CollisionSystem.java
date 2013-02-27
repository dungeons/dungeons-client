package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public class CollisionSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;

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
     * Checks for collision on given position. If object is entity is colliding, it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param size
     *            size of entity
     */
    protected void resolveMove(PositionComponent position, SizeComponent size) {

        float halfSize = size.size / 2f;
        float x = position.x;
        float y = position.y;

        float leftBound = x - halfSize;
        float rightBound = x + halfSize;
        float downBound = y - halfSize;
        float upBound = y + halfSize;

        Int leftPoint = new Point.Int((int) (leftBound / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));
        Int rightPoint = new Point.Int((int) (rightBound / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));
        Int downPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (downBound / App.MAZE_WALL_SIZE));
        Int upPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (upBound / App.MAZE_WALL_SIZE));

        if (!isWalkable(leftPoint)) {
            x = (leftPoint.x + 1) * App.MAZE_WALL_SIZE + halfSize;
        } else if (!isWalkable(rightPoint)) {
            x = rightPoint.x - halfSize;
        }

        if (!isWalkable(downPoint)) {
            y = (downPoint.y + 1) * App.MAZE_WALL_SIZE + halfSize;
        } else if (!isWalkable(upPoint)) {
            y = upPoint.y - halfSize;
        }

        position.x = x;
        position.y = y;
    }

    /**
     * Check whether is point in map bounds and walkable
     * 
     * @param point
     *            to be checked
     * @return {@code true} if point is walkable, {@code false} otherwise
     */
    private boolean isWalkable(Int point) {
        if (point.x < 0 || point.x >= App.getFootprint().length) {
            return false;
        }

        if (point.y < 0 || point.y >= App.getFootprint()[0].length) {
            return false;
        }

        return App.getFootprint()[point.x][point.y];
    }

}
