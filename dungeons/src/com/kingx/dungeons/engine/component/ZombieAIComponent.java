package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Component;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;

public class ZombieAIComponent extends Component {

    public final PositionComponent entityPosition;
    public final PositionComponent playerPosition;
    public final SpeedComponent entitySpeed;
    public final MoveComponent entityMove;
    public final ShaderComponent shader;

    public Color normalColor;
    public Color alertColor;
    public float alertRadius;

    public Vector3 target;

    public ZombieAIComponent(PositionComponent entityPosition, PositionComponent playerPosition, SpeedComponent entitySpeed, MoveComponent entityMove,
            ShaderComponent shader, Color normalColor, Color alertColor, float alertRadius) {
        this.entityPosition = entityPosition;
        this.playerPosition = playerPosition;
        this.entitySpeed = entitySpeed;
        this.entityMove = entityMove;
        this.shader = shader;
        this.normalColor = normalColor;
        this.alertColor = alertColor;
        this.alertRadius = alertRadius;
    }

}
