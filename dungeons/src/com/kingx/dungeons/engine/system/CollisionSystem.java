package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public class CollisionSystem extends EntitySystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;

    public CollisionSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, SizeComponent.class));
    }

    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        SizeComponent size = sizeMapper.get(e);
        resolveMove(position, size);
    }

    protected void resolveMove(PositionComponent position, SizeComponent size) {

        // TODO refactor this !
        float halfSize = size.size / 2f;
        // TODO refactor this !
        float x = position.x;
        float y = position.y;

        float left = x - halfSize;
        float right = x + halfSize;

        Int leftPoint = new Point.Int((int) (left / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));
        Int rightPoint = new Point.Int((int) (right / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));

        if (!isWalkable(leftPoint)) {
            x = (leftPoint.x + 1) * App.MAZE_WALL_SIZE + halfSize;
        } else if (!isWalkable(rightPoint)) {
            x = rightPoint.x - halfSize;
        }

        float down = y - halfSize;
        float up = y + halfSize;

        Int downPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (down / App.MAZE_WALL_SIZE));
        Int upPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (up / App.MAZE_WALL_SIZE));

        if (!isWalkable(downPoint)) {
            y = (downPoint.y + 1) * App.MAZE_WALL_SIZE + halfSize;
        } else if (!isWalkable(upPoint)) {
            y = upPoint.y - halfSize;
        }

        position.x = x;
        position.y = y;
    }

    private boolean isWalkable(Int point) {
        if (point.x < 0) {
            return false;
        } else if (point.x >= App.getFootprint().length) {
            return false;
        }

        if (point.y < 0) {
            return false;
        } else if (point.y >= App.getFootprint()[0].length) {
            return false;
        }
        return App.getFootprint()[point.x][point.y];
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
        return true;
    }

}
