package com.kingx.dungeons.engine.concrete;

import com.artemis.World;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.geom.MazePoly;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Maze extends ConcreteEntity {

    public Maze(World world, MazePoly maze) {
        super(world);
        bag.add(new PositionComponent(0, 0, 0));
        bag.add(new MeshComponent(maze.getMesh()));
        bag.add(new ShaderComponent(Shader.getShader("normal"), Colors.BASE, null));
    }
}
