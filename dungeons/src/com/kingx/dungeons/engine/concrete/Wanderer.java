package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Wanderer extends ConcreteEntity {

    final MoveComponent position;

    public Wanderer(World world, Vector3 position, float size, float speed, Camera camera) {
        super(world);

        PositionComponent positionComponent = new PositionComponent(position);
        MoveComponent moveComponent = new MoveComponent(0, 0);
        ShaderComponent shader = new ShaderComponent(Shader.getShader("normal"));
        TextureComponent textures = new TextureComponent("fish", "bloodyfish", Colors.AVATAR);
        HealthComponent health = new HealthComponent(100);
        SightComponent sight = new SightComponent(5f);

        bag.add(positionComponent);
        bag.add(new RotationComponent(0, 1, 0));
        bag.add(new SpeedComponent(speed));
        bag.add(moveComponent);
        bag.add(new SizeComponent(size));
        bag.add(shader);
        bag.add(textures);
        bag.add(health);
        bag.add(sight);
        bag.add(new ShadowComponent());

        if (camera != null) {
            bag.add(new FollowCameraComponent(camera, 10f));
        }

        this.position = moveComponent;

        //  InputManager.getInstance().registerInput(InputSet.Player1, moveComponent);
    }

    public MoveComponent getPosition() {
        return position;
    }

}
