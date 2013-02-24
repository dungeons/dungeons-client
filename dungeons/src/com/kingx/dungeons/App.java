package com.kingx.dungeons;

import java.util.ArrayList;
import java.util.List;

import com.artemis.World;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.controller.CameraController;
import com.kingx.dungeons.controller.PositionCamera;
import com.kingx.dungeons.engine.concrete.WandererCreation;
import com.kingx.dungeons.engine.system.MovementSystem;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.entity.CleverEntity;
import com.kingx.dungeons.entity.Ground;
import com.kingx.dungeons.entity.Maze;
import com.kingx.dungeons.entity.MazeFactory;
import com.kingx.dungeons.entity.Police;
import com.kingx.dungeons.entity.RenderableEntity;
import com.kingx.dungeons.entity.Wanderer;
import com.kingx.dungeons.geom.Point;

public class App implements ApplicationListener {

    private static Maze maze;
    private static CameraController followCamera;

    private static final List<RenderableEntity> renderList = new ArrayList<RenderableEntity>();
    private static final List<CleverEntity> updateList = new ArrayList<CleverEntity>();

    private static Ground ground;
    private static boolean[][] footprint;
    public static App reference;
    private static Wanderer wanderer;
    private static boolean wireframe;

    public static final int MAZE_BLOCKS_COUNT = 25;
    public static final float MAZE_WALL_SIZE = 1f;

    public static Wanderer getWanderer() {
        return wanderer;
    }

    public static Maze getMaze() {
        return maze;
    }

    public static List<RenderableEntity> getRenderList() {
        return renderList;
    }

    public static List<CleverEntity> getUpdateList() {
        return updateList;
    }

    @Override
    public void create() {
        reference = this;
        ShaderProgram.pedantic = false;

        world = new World();
        Logic logic = Logic.getInstance(this, world);

        Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL10.GL_LESS);

    }

    @Override
    public void dispose() {
    }

    private boolean glInit = false;
    private SpriteBatch sb;
    private World world;
    private RenderGeometrySystem renderGeometrySystem;

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
        renderList(renderList);

        renderGeometrySystem.process();

        //sb.begin();
        //sb.draw(ground.getCbt(), 0, 0, 100, 100, 1, 0, 0, 1);
        //sb.end();

    }

    private void init() {
        // Entities creation
        footprint = MazeFactory.getMaze(MAZE_BLOCKS_COUNT);
        maze = MazeFactory.getMazeShadow(footprint, MAZE_WALL_SIZE);
        wanderer = new Wanderer(maze);
        ground = new Ground(1000);

        ArrayList<Police> police = new ArrayList<Police>();
        for (int i = 0; i < 1000; i++) {
            police.add(new Police(maze));
        }

        // Adding to render list
        renderList.add(ground);
        renderList.add(maze);
        renderList.add(wanderer);
        renderList.addAll(police);

        // Adding to update list
        updateList.add(wanderer);
        updateList.addAll(police);

        followCamera.setController(wanderer);

        //   spriteRenderSystem = world.setSystem(new SpriteRenderSystem(camera), true);
        //  healthRenderSystem = world.setSystem(new HealthRenderSystem(camera), true);
        // hudRenderSystem = world.setSystem(new HudRenderSystem(camera), true);

        world.setSystem(new MovementSystem());
        renderGeometrySystem = world.setSystem(new RenderGeometrySystem(followCamera.getCamera()), true);
        world.initialize();

        Point.Int p = maze.getRandomBlock();

        WandererCreation wanderer2 = new WandererCreation(world, Maze.SIZE * (p.x + 0.5f), Maze.SIZE * (p.y + 0.5f), 0.2f, 5f);
        wanderer2.createEntity().addToWorld();

        //  for (int i = 0; 500 > i; i++) {
        //     EntityFactory.createStar(world).addToWorld();
        // }

    }

    public void update(float delta) {
        updateList(updateList, delta);
    }

    private void updateList(List<CleverEntity> list, float delta) {
        for (CleverEntity e : list) {
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
        camera.direction.set(0, 0, -1f);
        CameraController cameraController = new PositionCamera(camera);
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
