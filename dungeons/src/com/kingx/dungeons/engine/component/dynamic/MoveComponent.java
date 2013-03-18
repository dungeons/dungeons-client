package com.kingx.dungeons.engine.component.dynamic;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends AbstractComponent {
    public Vector2 vector;
    public Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();

    public MoveComponent(Vector2 vector) {
        this.vector = vector;
    }

    public MoveComponent(float x, float y) {
        this.vector = new Vector2(x, y);
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

}
