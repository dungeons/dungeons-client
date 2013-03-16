package com.kingx.dungeons.engine.component.dynamic;

import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends AbstractComponent {
    private final Vector2 vector;

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
        // TODO Auto-generated method stub

    }

    public Vector2 getVector() {
        return vector;
    }

}
