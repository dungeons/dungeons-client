package engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

import engine.component.PositionComponent;
import engine.component.SpeedComponent;

public class MovementSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<SpeedComponent> sm;

    public MovementSystem() {
        super(Aspect.getAspectFor(PositionComponent.class, SpeedComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = pm.get(e);
        SpeedComponent speed = sm.get(e);

        // TODO refactor this !
        x = puppet.getPositionX() + x;
        y = puppet.getPositionY() + y;

        float left = x - puppet.getHalfSize();
        float right = x + puppet.getHalfSize();

        Int leftPoint = new Point.Int((int) (left / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));
        Int rightPoint = new Point.Int((int) (right / App.MAZE_WALL_SIZE), (int) (y / App.MAZE_WALL_SIZE));

        if (!isWalkable(leftPoint)) {
            x = (leftPoint.x + 1) * App.MAZE_WALL_SIZE + puppet.getHalfSize();
        } else if (!isWalkable(rightPoint)) {
            x = rightPoint.x - puppet.getSize() / 2f;
        }

        float down = y - puppet.getHalfSize();
        float up = y + puppet.getHalfSize();

        Int downPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (down / App.MAZE_WALL_SIZE));
        Int upPoint = new Point.Int((int) (x / App.MAZE_WALL_SIZE), (int) (up / App.MAZE_WALL_SIZE));

        if (!isWalkable(downPoint)) {
            y = (downPoint.y + 1) * App.MAZE_WALL_SIZE + puppet.getHalfSize();
        } else if (!isWalkable(upPoint)) {
            y = upPoint.y - puppet.getSize() / 2f;
        }

        puppet.setPositionX(x);
        puppet.setPositionY(y);
    }

}
