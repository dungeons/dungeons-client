package com.kingx.dungeons.engine.component.ai;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;

public class ZombieAIComponent extends Component {

    public final PositionComponent entityPosition;
    public final PositionComponent playerPosition;
    public final ShaderComponent shader;

    public Color alertColor;
    public float alertRadius;

    public ZombieAIComponent(PositionComponent entityPosition, PositionComponent playerPosition, ShaderComponent shaderMapper, Color alertColor,
            float alertRadius) {
        this.entityPosition = entityPosition;
        this.playerPosition = playerPosition;
        this.shader = shaderMapper;
        this.alertColor = alertColor;
        this.alertRadius = alertRadius;
    }

}
