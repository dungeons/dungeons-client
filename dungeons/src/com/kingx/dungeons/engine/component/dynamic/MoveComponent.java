package com.kingx.dungeons.engine.component.dynamic;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;

public class MoveComponent extends AbstractComponent {
    public Vector3 vector;
    public float rotation = 0;
    public Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();

    public MoveComponent(Vector3 vector) {
        this.vector = vector;
    }

    public MoveComponent(float x, float y) {
        this.vector = new Vector3(x, y, 0);
    }

    @Override
    public String toString() {
        return "MoveComponent [vector=" + vector + "]";
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

    public Vector3 getRotatedVector() {
        return vector.cpy().rotate(Vector3.Y, rotation);
    }

    public float getRotation() {
        return rotation;
    }

    public void addRotation(float rotation) {
        this.rotation += rotation;
    }

}
