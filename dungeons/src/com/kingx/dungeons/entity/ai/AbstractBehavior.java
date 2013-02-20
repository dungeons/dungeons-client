package com.kingx.dungeons.entity.ai;

import com.kingx.dungeons.App;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;

public abstract class AbstractBehavior implements Behavior {

    protected Entity puppet;

    public AbstractBehavior() {
        this(null);
    }

    public AbstractBehavior(Entity puppet) {
        setPuppet(puppet);
    }

    @Override
    public void setPuppet(Entity puppet) {
        this.puppet = puppet;
    }

    @Override
    public void update(float delta) {
        move(delta);
    }

    protected void resolveMove(float x, float y) {

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

}
