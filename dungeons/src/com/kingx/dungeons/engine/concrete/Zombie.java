package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.MeshComponent;
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
    private final Mesh mesh;
    private Camera camera;

    public Zombie(World world, float x, float y, float size, float speed) {
        super(world);
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;

        mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
        mesh.setVertices(new float[] { -size / 2, -size / 2, 0, size / 2, -size / 2, 0, size / 2, size / 2, 0, -size / 2, size / 2, 0 });
        mesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
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
        MoveComponent zombieMove = new MoveComponent(0, 0);
        ShaderComponent shader = new ShaderComponent(Shader.getShader("normal"), Colors.ZOMBIE_NORMAL.color);
        e.addComponent(zombiePosition);
        e.addComponent(new RotationComponent(1, 0, 0));
        e.addComponent(new SpeedComponent(speed));
        e.addComponent(zombieMove);
        e.addComponent(new SizeComponent(size));
        e.addComponent(shader);
        e.addComponent(new MeshComponent(mesh));
        e.addComponent(new GeometryRenderTag());
        e.addComponent(new ZombieAIComponent(zombiePosition, playerPosition, zombieMove, shader, Colors.ZOMBIE_ALARM.color, 2f));
        return e;
    }
}
