package com.kingx.dungeons.engine.component;

import com.artemis.Component;

public class SpeedComponent extends Component {
    private final float current;
    private final float speed;
    private final float turbo;

    public SpeedComponent(float speed) {
        this.speed = speed;
        this.turbo = speed * 2;
        this.current = speed;
    }
}