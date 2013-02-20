package com.kingx.dungeons.trash;

import java.util.List;

import com.kingx.dungeons.App;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.ai.AbstractBehavior;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Float;
import com.kingx.dungeons.pathfinding.Map;
import com.kingx.dungeons.pathfinding.MapEntityNodeFactory;

public class MapAI extends AbstractBehavior {

    private static final float REGION = 1f;

    public MapAI() {
        this(null);
    }

    public MapAI(Entity puppet) {
        super(puppet);
    }

    public Point.Int getRegion() {
        return new Point.Int((int) ((puppet.getPositionX() + puppet.getSize()) / REGION), (int) ((puppet.getPositionY() + puppet.getSize()) / REGION));
    }

    private Point.Float goal = null;
    private List<Float> path;
    private int goalIndex = 0;

    private Float nextGoal() {
        if (path == null || goalIndex == path.size()) {
            path = getPath();
            goalIndex = 0;
        }
        goal = path.get(goalIndex++);

        // Goal is in center of region
        goal.x += REGION / 2f;
        goal.y += REGION / 2f;

        return goal;
    }

    @Override
    public void move(float delta) {
        if (goal == null) {
            goal = nextGoal();
        }
        System.out.println(delta);

        float step = puppet.getSpeed() * delta;
        System.out.println(step + " 87");
        moveIt(step);
    }

    private void moveIt(float step) {

        float tx = goal.x - puppet.getPositionX();
        float ty = goal.y - puppet.getPositionY();
        double ang = Math.atan2(ty, tx);

        double distance = Math.sqrt(tx * tx + ty * ty);

        if (step < distance) {
            System.out.println("move: 0 " + puppet.getPosition());
            System.out.println((float) (Math.cos(ang) * step));
            puppet.addPositionX((float) (Math.cos(ang) * step));
            puppet.addPositionY((float) (Math.sin(ang) * step));
            System.out.println("move: 1 " + puppet.getPosition());
        } else {
            step -= distance;
            puppet.setPositionX(goal.x);
            puppet.setPositionY(goal.y);
            goal = nextGoal();
            moveIt(step);
        }
    }

    public List<Float> getPath() {
        Map myMap = new Map(App.getMaze().getFootprint());
        myMap.setNode(new MapEntityNodeFactory());

        Point.Int start = getRegion();
        Point.Int finish = App.getMaze().getRandomBlock(start);

        System.out.print(start + " : " + finish);
        return myMap.findPath(start, finish);
    }

}
