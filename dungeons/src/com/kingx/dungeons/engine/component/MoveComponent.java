package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends Component {
    public Vector2 vector;

    public MoveComponent(Vector2 vector) {
        this.vector = vector;
    }

    public MoveComponent(float x, float y) {
        this.vector = new Vector2(x, y);
    }

}
