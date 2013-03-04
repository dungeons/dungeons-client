package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.MazeMap;
import com.kingx.dungeons.graphics.MazePolygon;
import com.kingx.dungeons.graphics.Shader;

public class Maze extends ConcreteEntity {

    private final Mesh mesh;
    private final ShaderProgram shader;

    public Maze(World world, MazeMap maze) {
        super(world);
        mesh = new MazePolygon(maze, new Vector3(1f, 1f, 1f)).generate();
        App.superhack = mesh;
        shader = Shader.getShader("normal");
    }

    @Override
    public Entity createEntity() {
        Entity e = this.getEntity();
        e.addComponent(new PositionComponent(0, 0, 0));
        e.addComponent(new MeshComponent(mesh));
        e.addComponent(new ShaderComponent(shader, Colors.BASE.color));
        return e;
    }
}
