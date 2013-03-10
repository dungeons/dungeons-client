package com.kingx.dungeons.engine.component;

import com.artemis.Component;

public class SpeedComponent extends Component {
    public float current;
    public float speed;
    public float turbo;

    public SpeedComponent(float speed) {
        this.speed = speed;
        this.turbo = speed * 2;
        this.current = speed;
    }
}