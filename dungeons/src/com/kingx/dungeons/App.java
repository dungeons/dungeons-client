package com.kingx.dungeons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.controller.CameraController;
import com.kingx.dungeons.controller.PositionCameraController;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.Ground;
import com.kingx.dungeons.entity.MazeFactory;
import com.kingx.dungeons.entity.MazeShadow;
import com.kingx.dungeons.entity.RenderableEntity;
import com.kingx.dungeons.entity.Wanderer;

public class App implements ApplicationListener {

    private static MazeShadow maze;
    private static CameraController followCamera;

    private List<RenderableEntity> renderList = new ArrayList<RenderableEntity>();
    private List<Entity> updateList = new ArrayList<Entity>();

    private Ground ground;
    private static boolean[][] footprint;
    public static App reference;
    private static Wanderer wanderer;
    private static boolean wireframe;

    public static final int MAZE_BLOCKS_COUNT = 10;
    public static final float MAZE_WALL_SIZE = 1f;

    public static Wanderer getWanderer() {
        return wanderer;
    }

    public static MazeShadow getMaze() {
        return maze;
    }

    @Override
    public void create() {
        reference = this;
        ShaderProgram.pedantic = false;

        Logic logic = Logic.getInstance(this);

        Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL10.GL_LESS);

    }

    /*
     * public List<Float> getPath(MapAI mapAI) { Map myMap = new Map(maze.getFootprint()); myMap.setNode(new MapEntityNodeFactory());
     * 
     * Point.Int start = mapAI.getRegion(); Point.Int finish = maze.getRandomBlock(start);
     * 
     * System.out.print(start + " : " + finish); return myMap.findPath(start, finish); }
     */
    @Override
    public void dispose() {
    }

    private boolean glInit = false;
    private SpriteBatch sb;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        if (!glInit) {
            init();
            glInit = true;
            sb = new SpriteBatch();
        }
        followCamera.getCamera().update();
        System.out.println(followCamera.getCamera().position);
        renderList(renderList);

        //sb.begin();
        //sb.draw(ground.getCbt(), 0, 0, 100, 100, 1, 0, 0, 1);
        //sb.end();

    }

    private void init() {
        // TODO Auto-generated method stub

        // Entities creation
        footprint = MazeFactory.getMaze(MAZE_BLOCKS_COUNT);
        maze = MazeFactory.getMazeShadow(footprint, MAZE_WALL_SIZE);
        System.out.println("wanderer");
        wanderer = new Wanderer(maze);
        ground = new Ground(1000);

        // Adding to render list
        renderList.add(ground);
        renderList.add(maze);
        renderList.add(wanderer);

        // Adding to update list
        updateList.add(wanderer);
        followCamera.setController(wanderer);
    }

    public void update(float delta) {
        updateList(updateList, delta);
    }

    private void updateList(List<Entity> list, float delta) {
        for (Entity e : list) {
            e.update(delta);
        }
    }

    private void renderList(List<RenderableEntity> list) {
        for (RenderableEntity e : list) {
            e.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (followCamera == null) {
            followCamera = setUpCamera(width, height);
        } else {
            followCamera.getCamera().viewportWidth = width;
            followCamera.getCamera().viewportHeight = height;
        }
    }

    public CameraController setUpCamera(int width, int height) {
        PerspectiveCamera camera = new PerspectiveCamera(67, width, height);
        camera.position.z = 15f;
        CameraController cameraController = new PositionCameraController(camera);
        return cameraController;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public static void toggleWireframe() {
        wireframe = !wireframe;
    }

    public static boolean isWireframe() {
        return wireframe;
    }

    public static Camera getDefaultCam() {
        return followCamera.getCamera();
    }

    public static boolean[][] getFootprint() {
        return footprint;
    }

}
