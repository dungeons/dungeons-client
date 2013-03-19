package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;

public class SightComponent extends Component {
    private float radius;

    public SightComponent(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

}
