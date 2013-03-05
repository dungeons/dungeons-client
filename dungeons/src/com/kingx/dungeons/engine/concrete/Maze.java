package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.geom.MazePoly;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Maze extends ConcreteEntity {

    private final MazePoly maze;
    private final ShaderProgram shader;

    public Maze(World world, MazePoly maze) {
        super(world);
        this.maze = maze;
        shader = Shader.getShader("normal");
    }

    @Override
    public Entity createEntity() {
        Entity e = this.getEntity();
        e.addComponent(new PositionComponent(0, 0, 0));
        e.addComponent(new MeshComponent(maze.getMesh()));
        e.addComponent(new ShaderComponent(shader, Colors.BASE.color));
        return e;
    }
}
