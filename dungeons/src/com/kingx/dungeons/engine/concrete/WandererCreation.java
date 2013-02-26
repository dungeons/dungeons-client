package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.InputComponent;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.input.InputManager;
import com.kingx.dungeons.engine.input.InputSet;
import com.kingx.dungeons.entity.graphics.Shader;

public class WandererCreation extends ConcreteEntity {

    private final World world;
    private final float x;
    private final float y;
    private final float size;
    private final float speed;
    private final Mesh mesh;
    private final ShaderProgram shader;
    private Camera camera;

    public WandererCreation(World world, float x, float y, float size, float speed) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;

        mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
        mesh.setVertices(new float[] { -size / 2, -size / 2, 0, size / 2, -size / 2, 0, size / 2, size / 2, 0, -size / 2, size / 2, 0 });
        mesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });

        shader = Shader.getShader("purple");
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Entity createEntity() {
        Entity e = world.createEntity();
        InputComponent ic = new InputComponent(0, 0);
        e.addComponent(new PositionComponent(x, y, size / 2f));
        e.addComponent(new SpeedComponent(speed));
        e.addComponent(ic);
        e.addComponent(new SizeComponent(size));
        e.addComponent(new MeshComponent(mesh));
        e.addComponent(new ShaderComponent(shader));

        if (camera != null) {
            e.addComponent(new FollowCameraComponent(camera, 10f));
        }

        InputManager.getInstance().registerInput(InputSet.Player1, ic);
        return e;
    }
}
