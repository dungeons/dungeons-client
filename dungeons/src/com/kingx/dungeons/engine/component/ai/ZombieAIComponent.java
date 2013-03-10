package com.kingx.dungeons.engine.component.ai;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;

public class ZombieAIComponent extends Component {

    public final PositionComponent entityPosition;
    public final PositionComponent playerPosition;
    public final MoveComponent entityMove;
    public final ShaderComponent shader;

    public Color alertColor;
    public float alertRadius;

    public Vector3 target;

    public ZombieAIComponent(PositionComponent entityPosition, PositionComponent playerPosition, MoveComponent entityMove, ShaderComponent shader,
            Color alertColor, float alertRadius) {
        this.entityPosition = entityPosition;
        this.playerPosition = playerPosition;
        this.entityMove = entityMove;
        this.shader = shader;
        this.alertColor = alertColor;
        this.alertRadius = alertRadius;
    }

}
