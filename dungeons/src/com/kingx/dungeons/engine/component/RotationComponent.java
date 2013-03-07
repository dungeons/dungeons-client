package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class RotationComponent extends Component {
    public final Vector3 vector;

    public RotationComponent(Vector3 vector) {
        this.vector = vector;
    }

    public RotationComponent(float x, float y, float z) {
        this.vector = new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "RotationComponent [vector=" + vector + "]";
    }

}
