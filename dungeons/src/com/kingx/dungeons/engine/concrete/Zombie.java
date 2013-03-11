package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.RotationComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.ai.ZombieAIComponent;
import com.kingx.dungeons.engine.tags.GeometryRenderTag;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Zombie extends ConcreteEntity {

    private final float x;
    private final float y;
    private final float size;
    private final float speed;
    private Camera camera;

    public Zombie(World world, float x, float y, float size, float speed) {
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
        PositionComponent zombiePosition = new PositionComponent(x, y, size / 2f);
        PositionComponent playerPosition = App.getPlayer().getEntity().getComponent(PositionComponent.class);
        SpeedComponent zombieSpeed = new SpeedComponent(speed);
        MoveComponent zombieMove = new MoveComponent(0, 0);
        ShaderComponent shader = new ShaderComponent(Shader.getShader("normal"), Colors.ZOMBIE_NORMAL.color, Assets.getTexture("zombie", 0));
        e.addComponent(zombiePosition);
        e.addComponent(new RotationComponent(1, 0, 0));
        e.addComponent(zombieSpeed);
        e.addComponent(zombieMove);
        e.addComponent(new SizeComponent(size));
        e.addComponent(shader);
        e.addComponent(new GeometryRenderTag());
        e.addComponent(new ZombieAIComponent(zombiePosition, playerPosition, zombieSpeed, zombieMove, shader, Colors.ZOMBIE_ALARM.color, 4f));
        return e;
    }
}
