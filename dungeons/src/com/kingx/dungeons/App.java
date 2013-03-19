package com.kingx.dungeons;

import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.concrete.Maze;
import com.kingx.dungeons.engine.concrete.Wanderer;
import com.kingx.dungeons.engine.concrete.Zombie;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.engine.system.RenderShadowSystem;
import com.kingx.dungeons.geom.MazeBuilder;
import com.kingx.dungeons.geom.MazeFactory;
import com.kingx.dungeons.geom.MazePoly;
import com.kingx.dungeons.graphics.MazeMap;
import com.kingx.dungeons.input.Input;
import com.kingx.dungeons.server.AbstractServer;
import com.kingx.dungeons.server.OfflineServer;
import com.kingx.dungeons.server.OnlineServer;

public class App implements ApplicationListener {

    // Global parameters
    public static Param DEBUG;
    public static Param NOSLEEP;
    public static Param SERVER;

    public static final Random rand = new Random();
    private static Camera camera;

    private static MazeMap mazeMap;
    public static MazePoly mazeMesh;

    private static boolean wireframe;
    private static boolean fps;

    public static final int MAZE_BLOCKS_COUNT = 20;
    public static final float MAZE_WALL_SIZE = 1f;

    private final Map<String, Param> params;
    private Clock clock;

    public App(String[] args) {
        params = Param.getParams(args);
        DEBUG = getParam("-d", "-debug");
        NOSLEEP = getParam("-ns", "-nosleep");
        SERVER = getParam("-s", "-server");
    }

    private Param getParam(String... args) {
        for (String arg : args) {
            Param param = params.get(arg);
            if (param != null) {
                return param;
            }
        }
        return null;
    }

    @Override
    public void create() {
        ShaderProgram.pedantic = false;
        world = new World();

        Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL10.GL_LESS);

        clock = new Clock();
        Gdx.input.setInputProcessor(new Input());
    }

    public static boolean INITIALIZED = false;
    private SpriteBatch onScreenRender;
    private World world;
    private RenderShadowSystem renderShadowSystem;
    private RenderGeometrySystem renderGeometrySystem;
    private static Wanderer player;
    private static AbstractServer server;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        if (!INITIALIZED) {
            init();
            INITIALIZED = true;
        }
        camera.update();

        renderShadowSystem.process();
        renderGeometrySystem.process();

        if (DEBUG != null && renderShadowSystem.getDepthMap() != null) {
            onScreenRender.begin();
            onScreenRender.draw(renderShadowSystem.getDepthMap(), 0, 0, 100, 100, 1, 0, 0, 1);
            onScreenRender.end();
        }

    }

    private void init() {
        addSystemsToWorld();

        createMaze();
        createPlayer();
        createZombies(5);

        onScreenRender = new SpriteBatch();
        server = SERVER != null ? new OnlineServer(world) : new OfflineServer(world);
        world.initialize();

        clock.addService(server);
    }

    /**
     * Generates maze footprint and polygon. Creates maze instance and places it
     * in the game world.
     */
    private void createMaze() {
        mazeMap = new MazeMap(createMap());
        mazeMesh = new MazeFactory(mazeMap, new Vector3(1f, 1f, 1f)).generate();
        Maze mazeCreation = new Maze(world, mazeMesh);
        mazeCreation.createEntity().addToWorld();
    }

    /**
     * If template is available, creates footprint based on that template,
     * otherwise generates random map.
     * 
     * @return generated map
     */
    private boolean[][] createMap() {
        return Assets.map == null ? MazeBuilder.getMaze(MAZE_BLOCKS_COUNT) : Assets.map;
    }

    /**
     * Register systems to the world and initialize.
     */
    private void addSystemsToWorld() {
        renderShadowSystem = world.setSystem(new RenderShadowSystem(camera), true);
        renderGeometrySystem = world.setSystem(new RenderGeometrySystem(camera), true);
    }

    /**
     * Place player in the game
     */
    private void createPlayer() {
        Vector3 p = mazeMap.getRandomPosition();
        player = new Wanderer(world, p, 1f, 5f, camera);
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
            Vector3 p = mazeMap.getRandomPosition();
            Zombie zombie = new Zombie(world, p, 1f, 1f);
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

    public static MazeMap getMap() {
        return mazeMap;
    }

    public static Camera getDefaultCam() {
        return camera;
    }

    public static AbstractServer getServer() {
        return server;
    }

    // Application cycle events

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
