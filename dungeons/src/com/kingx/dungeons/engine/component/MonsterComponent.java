package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class MonsterComponent extends Component {
    public Color alertColor;
    public float alertRadius;

    public MonsterComponent(Color alertColor, float alertRadius) {
        this.alertColor = alertColor;
        this.alertRadius = alertRadius;
    }

}
