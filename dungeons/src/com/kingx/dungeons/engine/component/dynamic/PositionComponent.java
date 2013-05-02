package com.kingx.dungeons.engine.component.dynamic;

import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;

public class PositionComponent extends AbstractComponent {
    public Vector3 inWorld;
    private boolean animation = false;
    private MovementType type = MovementType.WALK;

    public PositionComponent(float x, float y, float z) {
        this.inWorld = new Vector3(x, y, z);
    }

    public PositionComponent(Vector3 vector) {
        this.inWorld = vector.cpy();
    }

    public float getX() {
        return inWorld.x;
    }

    public float getY() {
        return inWorld.y;
    }

    public float getZ() {
        return inWorld.z;
    }

    public void setX(float x) {
        inWorld.x = x;
    }

    public void setY(float y) {
        inWorld.y = y;
    }

    public void setZ(float z) {
        inWorld.z = z;
    }

    @Override
    public void setComponent(int id, int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return inWorld.toString();
    }

    public void set(Vector3 vec) {
        this.inWorld.set(vec);
    }

    public Vector3 get() {
        return inWorld;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public enum MovementType {
        WALK,
        CLIMB;
    }

    public void setMovementType(MovementType type) {
        this.type = type;
    }

    public MovementType getMovementType() {
        return this.type;
    }

    public boolean canClimb() {
        return this.getY() < App.getTerrain().getHeight();
    }
}
