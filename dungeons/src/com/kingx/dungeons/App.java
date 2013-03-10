package com.kingx.dungeons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import com.artemis.World;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.engine.concrete.Maze;
import com.kingx.dungeons.engine.concrete.Wanderer;
import com.kingx.dungeons.engine.concrete.Zombie;
import com.kingx.dungeons.engine.system.CollisionSystem;
import com.kingx.dungeons.engine.system.MovementSystem;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.engine.system.RenderShadowSystem;
import com.kingx.dungeons.engine.system.ai.ZombieAI;
import com.kingx.dungeons.geom.MazeBuilder;
import com.kingx.dungeons.geom.MazePoly;
import com.kingx.dungeons.geom.Point;
import com.kingx.dungeons.geom.Point.Int;
import com.kingx.dungeons.graphics.MazeMap;
import com.kingx.dungeons.graphics.MazePolygon;

public class App implements ApplicationListener {

    // Global switches
    public static boolean DEBUG;
    public static boolean NOSLEEP;

    public static final Random rand = new Random();
    private static Camera camera;

    private static boolean[][] footprint;
    public static App reference;
    private static boolean wireframe;
    private static boolean fps;
    public static MazePoly mazeMesh;
    public static int rot;

    public static final int MAZE_BLOCKS_COUNT = 25;
    public static final float MAZE_WALL_SIZE = 1f;
    private final HashSet<String> params;

    public App(String[] args) {
        params = new HashSet<String>();
        if (args != null) {
            params.addAll(Arrays.asList(args));
        }
        DEBUG = params.contains("debug") || params.contains("-debug") || params.contains("--debug");
        NOSLEEP = params.contains("nosleep") || params.contains("-nosleep") || params.contains("--nosleep");
    }

    @Override
    public void create() {
        reference = this;
        ShaderProgram.pedantic = false;

        world = new World();
        Logic.init(world);

        Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL10.GL_LESS);
    }

    private boolean initialized = false;
    private SpriteBatch onScreenRender;
    private World world;
    private RenderGeometrySystem renderGeometrySystem;
    private RenderShadowSystem renderShadowSystem;
    private MazeMap maze;
    private static Wanderer player;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        if (!initialized) {
            init();
        }
        camera.update();

        renderGeometrySystem.process();
        renderShadowSystem.process();

        if (DEBUG && renderShadowSystem.getDepthMap() != null) {
            onScreenRender.begin();
            onScreenRender.draw(renderShadowSystem.getDepthMap(), 0, 0, 100, 100, 1, 0, 0, 1);
            onScreenRender.end();
        }

    }

    private void init() {
        createMap();
        createMaze();
        addSystemsToWorld();

        createPlayer();
        createZombies(10);

        Maze mazeCreation = new Maze(world, mazeMesh);
        mazeCreation.createEntity().addToWorld();

        onScreenRender = new SpriteBatch();

        initialized = true;

    }

    /**
     * If template is available, creates footprint based on that template,
     * otherwise generates random map.
     */
    private void createMap() {
        footprint = Assets.map == null ? MazeBuilder.getMaze(MAZE_BLOCKS_COUNT) : Assets.map;
    }

    /**
     * Build maze polygon from footprint.
     */
    private void createMaze() {
        maze = new MazeMap(footprint);
        mazeMesh = new MazePolygon(maze, new Vector3(1f, 1f, 1f)).generate();
    }

    /**
     * Register systems to the world and initialize.
     */
    private void addSystemsToWorld() {
        world.setSystem(new MovementSystem());
        world.setSystem(new ZombieAI());
        world.setSystem(new CollisionSystem());
        renderShadowSystem = world.setSystem(new RenderShadowSystem(camera), true);
        renderGeometrySystem = world.setSystem(new RenderGeometrySystem(camera), true);
        world.initialize();
    }

    /**
     * Place player in the game
     */
    private void createPlayer() {
        Point.Int p = maze.getRandomBlock();
        player = new Wanderer(world, 1f * (p.x + 0.5f), 1f * (p.y + 0.5f), 0.2f, 5f);
        player.setCamera(camera);
        player.createEntity().addToWorld();
    }

    /**
     * Scatter zombies all over the map.
     * 
     * @param count
     *            number of zombies
     */
    private void createZombies(int count) {
        for (int i = 0; i < count; i++) {
            Int p = maze.getRandomBlock();
            Zombie zombie = new Zombie(world, 1f * (p.x + 0.5f), 1f * (p.y + 0.5f), 0.2f, 1f);
            zombie.createEntity().addToWorld();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (camera == null) {
            camera = setUpCamera(width, height);
        } else {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
        }
    }

    public Camera setUpCamera(int width, int height) {
        PerspectiveCamera camera = new PerspectiveCamera(67, width, height);
        camera.position.z = 15f;
        camera.direction.set(0, 0, -1f);
        return camera;
    }

    // Toggle switches

    public static void toggleWireframe() {
        wireframe = !wireframe;
    }

    public static boolean isWireframe() {
        return wireframe;
    }

    public static void toggleFps() {
        fps = !fps;
    }

    public static boolean isFps() {
        return fps;
    }

    // Global getters

    public static MazePoly getMaze() {
        return mazeMesh;
    }

    public static Wanderer getPlayer() {
        return player;
    }

    public static boolean[][] getFootprint() {
        return footprint;
    }

    public static Camera getDefaultCam() {
        return camera;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
