package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent extends Component {
    public final Vector3 vector;

    public PositionComponent(float x, float y, float z) {
        this.vector = new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "PositionComponent [x=" + vector.x + ", y=" + vector.y + ", z=" + vector.z + "]";
    }

}
