package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.MonsterComponent;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.tags.GeometryRenderTag;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Zombie extends ConcreteEntity {

    private final float x;
    private final float y;
    private final float size;
    private final float speed;
    private final Mesh mesh;
    private final ShaderProgram shader;
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

        shader = Shader.getShader("normal");
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
        PositionComponent pc = new PositionComponent(x, y, size / 2f);
        e.addComponent(pc);
        e.addComponent(new SpeedComponent(speed));
        e.addComponent(moveComponent);
        e.addComponent(new SizeComponent(size));
        e.addComponent(new ShaderComponent(shader, Colors.ZOMBIE_NORMAL.color));
        e.addComponent(new MeshComponent(mesh));
        e.addComponent(new GeometryRenderTag());
        e.addComponent(new MonsterComponent(Colors.ZOMBIE_ALARM.color, 3f));
        return e;
    }
}
