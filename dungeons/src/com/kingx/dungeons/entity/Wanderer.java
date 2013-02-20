package com.kingx.dungeons.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.Input;
import com.kingx.dungeons.controller.EyesCameraController;
import com.kingx.dungeons.entity.ai.PlayerControlledBehavior;
import com.kingx.dungeons.entity.graphics.Shader;
import com.kingx.dungeons.geom.Point;

public class Wanderer extends RenderableEntity {

    private Mesh mesh;
    private ShaderProgram shader;
    private PlayerControlledBehavior avatar;
    private EyesCameraController[] eyes;

    public Wanderer(MazeShadow maze) {
	super(0, 0, 0, 0.2f, 0.05f);
	Point.Int p = maze.getRandomBlock();
	this.setPositionX(Maze.SIZE * (p.x + 0.5f));
	this.setPositionY(Maze.SIZE * (p.y + 0.5f));
	this.setPositionZ(Maze.SIZE * 0.5f);

	avatar = Input.getInstance().getControlledEntity(this);

/*
 * camera.direction.x = 0;
 * camera.direction.y = -.5f;
 * camera.direction.z = -1;
 */
	eyes = new EyesCameraController[4];
	int offset = 1;
	for (int i = 0; i < eyes.length; i++) {
	    Camera camera = new PerspectiveCamera(90, 128, 128);
	    camera.near = 0.0001f;
	    camera.far = 500;
	    camera.direction.x = (float) Math.round(Math.cos(Math.PI/2 * (i + offset)));
	    camera.direction.y = (float) Math.round(Math.sin(Math.PI/2 * (i + offset)));
	    camera.direction.z = 0.001f;
	    camera.position.z = 0.1f;
	    eyes[i] = new EyesCameraController(camera);
	    eyes[i].setController(this);
	}

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
	shader = Shader.getShader("wanderer");
    }

    @Override
    protected void doUpdate(float delta) {
	System.out.println("updating");
	avatar.update(delta);
    }

    public Camera getEyes(int index) {
	return index >= 0 && index < eyes.length ? eyes[index].getCamera() : null;
    }

    public int getEyesCount() {
	return eyes.length;
    }

}
