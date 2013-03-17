package com.kingx.dungeons.engine.component.dynamic;

public class SizeComponent extends AbstractComponent {
    private float size;

    public SizeComponent(float size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SizeComponent [size=" + size + "]";
    }

    @Override
    public void setComponent(int id, int value) {
        size = value / AbstractComponent.INT_TO_FLOAT;
    }

    public float getSize() {
        return size;
    }

}
