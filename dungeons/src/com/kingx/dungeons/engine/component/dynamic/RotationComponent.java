package com.kingx.dungeons.engine.component.dynamic;

import com.badlogic.gdx.math.Vector3;

public class RotationComponent extends AbstractComponent {
    private Vector3 vector;

    public RotationComponent(Vector3 vector) {
        this.vector = vector;
    }

    public RotationComponent(float x, float y, float z) {
        this.vector = new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "RotationComponent [vector=" + vector + "]";
    }

    @Override
    public void setComponent(int id, int value) {
        switch (id) {
            case 0:
                this.vector.x = value / AbstractComponent.INT_TO_FLOAT;
                break;
            case 1:
                this.vector.y = value / AbstractComponent.INT_TO_FLOAT;
                break;
        }

    }

    public float getX() {
        return vector.x;
    }

    public float getY() {
        return vector.y;
    }

    public Vector3 getVector() {
        return vector;
    }

    public void setVector(Vector3 vector) {
        this.vector = vector;
    }

}
