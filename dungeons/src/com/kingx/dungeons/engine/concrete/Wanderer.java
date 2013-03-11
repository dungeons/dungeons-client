package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.RotationComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.tags.GeometryRenderTag;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;
import com.kingx.dungeons.input.InputManager;
import com.kingx.dungeons.input.InputSet;

public class Wanderer extends ConcreteEntity {

    private final float x;
    private final float y;
    private final float size;
    private final float speed;
    private Camera camera;

    public Wanderer(World world, float x, float y, float size, float speed) {
        super(world);
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;

    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Entity createEntity() {
        Entity e = getEntity();
        MoveComponent moveComponent = new MoveComponent(0, 0);
        e.addComponent(new PositionComponent(x, y, size / 2f));
        e.addComponent(new RotationComponent(0, 1, 0));
        e.addComponent(new SpeedComponent(speed));
        e.addComponent(moveComponent);
        e.addComponent(new SizeComponent(size));
        e.addComponent(new ShaderComponent(Shader.getShader("normal"), Colors.AVATAR.color, Assets.getTexture("zombie", 1)));
        e.addComponent(new ShadowComponent());
        e.addComponent(new GeometryRenderTag());

        if (camera != null) {
            e.addComponent(new FollowCameraComponent(camera, 10f));
        }

        InputManager.getInstance().registerInput(InputSet.Player1, moveComponent);
        return e;
    }
}
