package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector2;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.WorldRotateComponent;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Wanderer extends ConcreteEntity {

    private final PositionComponent positionComponent;
    private final MoveComponent moveComponent;
    private final ShaderComponent shader;
    private final TextureComponent textures;
    private final HealthComponent health;
    private final SightComponent sight;
    private final GravityComponent gravity;
    private final WorldRotateComponent worldRotate;

    public Wanderer(World world, Vector2 p, float size, float speed, FollowCameraComponent camera) {
        super(world);

        positionComponent = new PositionComponent(p.x, p.y, 0.5f);
        moveComponent = new MoveComponent(0, 0);
        shader = new ShaderComponent(Shader.getShader("normal"));
        textures = new TextureComponent("wanderer", "wanderer", Colors.AVATAR);
        health = new HealthComponent(100);
        sight = new SightComponent(5f);
        gravity = new GravityComponent(3f, moveComponent);
        SizeComponent sizeComponent = new SizeComponent(size);
        worldRotate = new WorldRotateComponent(positionComponent, moveComponent, sizeComponent);

        bag.add(positionComponent);
        bag.add(new RotationComponent(0, 1, 0));
        bag.add(new SpeedComponent(speed));
        bag.add(moveComponent);
        bag.add(sizeComponent);
        bag.add(shader);
        bag.add(textures);
        bag.add(health);
        bag.add(sight);
        bag.add(gravity);
        bag.add(worldRotate);
        bag.add(new ShadowComponent());

        if (camera != null) {
            bag.add(camera);
        }
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public MoveComponent getMoveComponent() {
        return moveComponent;
    }

    public ShaderComponent getShader() {
        return shader;
    }

    public TextureComponent getTextures() {
        return textures;
    }

    public HealthComponent getHealth() {
        return health;
    }

    public SightComponent getSight() {
        return sight;
    }

    public GravityComponent getGravity() {
        return gravity;
    }

    public WorldRotateComponent getWorldRotate() {
        return worldRotate;
    }

}
