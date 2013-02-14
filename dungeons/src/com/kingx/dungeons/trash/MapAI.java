package com.kingx.dungeons.trash;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.ai.AbstractBehavior;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Float;


public abstract class MapAI extends AbstractBehavior {

    private static final float REGION = 1f;
    private float speed;
    

    public MapAI() {
        this(null);
    }

    public MapAI(Entity puppet) {
        super(puppet);
    }

 /*   

    public Point.Int getRegion() {
        return new Point.Int((int) ((puppet.getPositionX() + puppet.getSize()) / REGION), (int) ((puppet.getPositionY()  + puppet.getSize()) / REGION));
    }

    private Point.Float goal = null;
    private List<Float> path;
    private int goalIndex = 0;


    private Float nextGoal() {
        if(path == null || goalIndex == path.size()){
            path = App.reference.getPath(this);
            goalIndex = 0;
        }
        goal = path.get(goalIndex++);

        // Goal is in center of region
        goal.x += REGION / 2f;
        goal.y += REGION / 2f;

        return goal;
    }

    @Override
    protected void doUpdate(float delta) {
        if (goal == null) {
            goal = nextGoal();
        }

        float step = speed * delta;
        move(step);
    }

    private void move(float step) {

        float tx = goal.x - entity.getPositionX() ;
        float ty = goal.y - entity.getPositionY() ;
        double ang = Math.atan2(ty, tx);

        double distance = Math.sqrt(tx * tx + ty * ty);

        if (step < distance) {
            entity.addPositionX((float) (Math.cos(ang) * step));
            entity.addPositionY((float) (Math.sin(ang) * step));
        } else {
            step -= distance;
            entity.setPositionX(goal.x);
            entity.setPositionY(goal.y);
            goal = nextGoal();
            move(step);
        }
    }
    */
    
    
}
