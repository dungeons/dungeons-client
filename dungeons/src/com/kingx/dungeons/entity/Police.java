package com.kingx.dungeons.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.entity.ai.Behavior;
import com.kingx.dungeons.entity.graphics.Shader;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.trash.MapAI;

public class Police extends ShadowCastingEntity {

    private Mesh mesh;
    private ShaderProgram shader;
    private final Behavior patrol;

    public Police(Maze maze) {
        super(0, 0, 0, 0.2f, 5f);
        Point.Int p = maze.getRandomBlock();
        this.setPositionX(Maze.SIZE * (p.x + 0.5f));
        this.setPositionY(Maze.SIZE * (p.y + 0.5f));
        this.setPositionZ(Maze.SIZE * 0.5f);

        patrol = new MapAI(this);
        setBehavior(patrol);

        this.setActive(true);
    }

    @Override
    protected void doRender(Camera cam) {
        cam.combined.translate(this.getPositionX(), this.getPositionY(), this.getPositionZ());

        shader.begin();
        shader.setUniformMatrix("u_MVPMatrix", cam.combined);
        mesh.render(shader, GL10.GL_TRIANGLES);
        shader.end();

        cam.combined.translate(-this.getPositionX(), -this.getPositionY(), -this.getPositionZ());
    }

    @Override
    protected void initRender() {
        mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
        mesh.setVertices(new float[] { -this.getHalfSize(), -this.getHalfSize(), 0, this.getHalfSize(), -this.getHalfSize(), 0, this.getHalfSize(),
                this.getHalfSize(), 0, -this.getHalfSize(), this.getHalfSize(), 0 });
        mesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
        shader = Shader.getShader("normal");
    }

}
