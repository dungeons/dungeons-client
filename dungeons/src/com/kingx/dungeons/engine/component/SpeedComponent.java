package com.kingx.dungeons.engine.component;

import com.artemis.Component;

public class SpeedComponent extends Component {
    private float current;
    public final float normal;
    public final float turbo;

    public SpeedComponent(float speed) {
        this.normal = speed;
        this.turbo = speed * 2;
        this.current = speed;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

}