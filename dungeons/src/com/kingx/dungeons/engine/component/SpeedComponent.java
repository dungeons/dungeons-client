package com.kingx.dungeons.engine.component;

import com.artemis.Component;

public class SpeedComponent extends Component {
    public float speed;

    public SpeedComponent(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "SpeedComponent [speed=" + speed + "]";
    }

}