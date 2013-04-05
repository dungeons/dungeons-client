package com.kingx.dungeons.engine.component.dynamic;

import com.badlogic.gdx.math.Vector3;

public class SizeComponent extends AbstractComponent {
    private final Vector3 vector;

    public SizeComponent(float size) {
        this.vector = new Vector3(size, size, 0);
    }

    public SizeComponent(float height, float width) {
        this.vector = new Vector3(height, width, 0);
    }

    @Override
    public void setComponent(int id, int value) {
        vector.x = value / AbstractComponent.INT_TO_FLOAT;
        vector.y = value / AbstractComponent.INT_TO_FLOAT;
    }

    public float getSize() {
        return vector.x;
    }

    public Vector3 get() {
        return vector;
    }

}
