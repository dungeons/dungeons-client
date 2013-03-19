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
    public final TextureComponent texture;

    public Color normalColor;
    public Color alertColor;
    public SightComponent sight;

    public boolean seeTarget = false;
    public Vector3 targetPosition = null;

    public ZombieAIComponent(PositionComponent entityPosition, PositionComponent playerPosition, SpeedComponent entitySpeed, MoveComponent entityMove,
            ShaderComponent shader, TextureComponent texture, Color normalColor, Color alertColor, SightComponent sight) {
        this.entityPosition = entityPosition;
        this.playerPosition = playerPosition;
        this.entitySpeed = entitySpeed;
        this.entityMove = entityMove;
        this.shader = shader;
        this.texture = texture;
        this.normalColor = normalColor;
        this.alertColor = alertColor;
        this.sight = sight;
    }

}
