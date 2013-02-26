package com.kingx.dungeons;

import java.util.Random;

import com.artemis.World;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.engine.concrete.Maze;
import com.kingx.dungeons.engine.concrete.Wanderer;
import com.kingx.dungeons.engine.system.CollisionSystem;
import com.kingx.dungeons.engine.system.MovementSystem;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.engine.system.RenderShadowSystem;
import com.kingx.dungeons.entity.MazeBuilder;
import com.kingx.dungeons.entity.graphics.MazeMap;
import com.kingx.dungeons.geom.Point;

public class App implements ApplicationListener {

    public static final Random rand = new Random();
    private static Camera followCamera;

    // private static final List<RenderableEntity> renderList = new ArrayList<RenderableEntity>();
    // private static final List<CleverEntity> updateList = new ArrayList<CleverEntity>();

    private static boolean[][] footprint;
    public static App reference;
    private static boolean wireframe;
    public static Mesh superhack;

    public static final int MAZE_BLOCKS_COUNT = 25;
    public static final float MAZE_WALL_SIZE = 1f;

    public static Mesh getMaze() {
        return superhack;
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
    private RenderShadowSystem renderShadowSystem;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        if (!glInit) {
            init();
            glInit = true;
            sb = new SpriteBatch();
        }
        followCamera.update();
        //   renderList(renderList);

        renderShadowSystem.process();
        renderGeometrySystem.process();

        if (renderShadowSystem.getCbt() != null) {
            sb.begin();
            sb.draw(renderShadowSystem.getCbt(), 0, 0, 200, 200, 1, 0, 0, 1);
            sb.end();
        }

    }

    private void init() {
        // Entities creation
        footprint = Assets.map;
        if (Assets.map == null) {
            footprint = MazeBuilder.getMaze(MAZE_BLOCKS_COUNT);
        }
        MazeMap maze = new MazeMap(footprint);

        /* for (int i = 0; i < 1000; i++) {
             police.add(new Police(maze));
         }*/

        // Adding to render list
        //renderList.add(ground);
        // renderList.add(mazeEntity);
        //renderList.add(wanderer);
        //renderList.addAll(police);

        // Adding to update list
        // updateList.add(wanderer);
        // updateList.addAll(police);

        //followCamera.setController(wanderer);

        //   spriteRenderSystem = world.setSystem(new SpriteRenderSystem(camera), true);
        //  healthRenderSystem = world.setSystem(new HealthRenderSystem(camera), true);
        // hudRenderSystem = world.setSystem(new HudRenderSystem(camera), true);

        world.setSystem(new MovementSystem());
        world.setSystem(new CollisionSystem());
        // world.setSystem(new PositionCameraSystem(followCamera));
        renderShadowSystem = world.setSystem(new RenderShadowSystem(followCamera), true);
        renderGeometrySystem = world.setSystem(new RenderGeometrySystem(followCamera), true);
        world.initialize();

        Point.Int p = maze.getRandomBlock();

        Wanderer wanderer2 = new Wanderer(world, 1f * (p.x + 0.5f), 1f * (p.y + 0.5f), 0.2f, 5f);
        Maze mazeCreation = new Maze(world, maze);
        wanderer2.setCamera(followCamera);
        wanderer2.createEntity().addToWorld();
        mazeCreation.createEntity().addToWorld();

        //  for (int i = 0; 500 > i; i++) {
        //     EntityFactory.createStar(world).addToWorld();
        // }

    }

    /*    public void update(float delta) {
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
        }*/

    @Override
    public void resize(int width, int height) {
        if (followCamera == null) {
            followCamera = setUpCamera(width, height);
        } else {
            followCamera.viewportWidth = width;
            followCamera.viewportHeight = height;
        }
    }

    public Camera setUpCamera(int width, int height) {
        PerspectiveCamera camera = new PerspectiveCamera(67, width, height);
        camera.position.z = 15f;
        camera.direction.set(0, 0, -1f);
        return camera;
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
        return followCamera;
    }

    public static boolean[][] getFootprint() {
        return footprint;
    }

}
