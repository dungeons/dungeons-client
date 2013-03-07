package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.geom.MazePoly;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Maze extends ConcreteEntity {

    private final MazePoly maze;

    public Maze(World world, MazePoly maze) {
        super(world);
        this.maze = maze;
    }

    @Override
    public Entity createEntity() {
        Entity e = this.getEntity();
        e.addComponent(new PositionComponent(0, 0, 0));
        e.addComponent(new MeshComponent(maze.getMesh()));
        e.addComponent(new ShaderComponent(Shader.getShader("normal"), Colors.BASE.color));
        return e;
    }
}
