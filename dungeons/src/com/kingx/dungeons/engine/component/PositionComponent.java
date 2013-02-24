package com.kingx.dungeons.engine.component;

import com.artemis.Component;

public class PositionComponent extends Component {
    public float x, y, z;

    public PositionComponent(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
