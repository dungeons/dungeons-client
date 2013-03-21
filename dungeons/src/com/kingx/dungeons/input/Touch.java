package com.kingx.dungeons.input;

import com.badlogic.gdx.math.Vector3;

public class Touch {

    private final Vector3 point;
    private boolean pressed;

    public Touch() {
        this(0, 0);
    }

    public Touch(float x, float y) {
        this(x, y, false);
    }

    public Touch(float x, float y, boolean pressed) {
        this.point = new Vector3(x, y, 0);
        this.pressed = pressed;
    }

    public Vector3 getPoint() {
        return point;
    }

    public boolean isPressed() {
        return pressed;
    }

    public float getX() {
        return point.x;
    }

    public float getY() {
        return point.y;
    }

    public void setX(float x) {
        point.x = x;
    }

    public void setY(float y) {
        point.y = y;
    }

    public void press() {
        pressed = true;
    }

    public void release() {
        pressed = false;
    }

}
