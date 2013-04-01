package com.kingx.dungeons;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.concrete.Wanderer;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.engine.system.RenderShadowSystem;
import com.kingx.dungeons.generator.GeneratorFactory;
import com.kingx.dungeons.generator.GeneratorType;
import com.kingx.dungeons.graphics.MazeMap;
import com.kingx.dungeons.graphics.cube.CubeFactory;
import com.kingx.dungeons.graphics.cube.CubeManager;
import com.kingx.dungeons.graphics.cube.CubeRegion;
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
    private static FollowCameraComponent worldCamera;
    private static FollowCameraComponent avatarCamera;

    private static MazeMap mazeMap;
    private static CubeManager cubeManager;

    public static final float PLAYER_OFFSET = 0.5f;
    public static final float LIGHT_OFFSET = 0.1f;

    private static boolean wireframe;

    public static final float UNIT = 1f;

    private final Map<String, Param> params;
    private Clock clock;

    public App(String[] args) {
        params = args != null ? Param.getParams(args) : Param.getParams(new String[] {});
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
    private static int currentView;
    BitmapFont font = null;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        if (!INITIALIZED) {
            init();
            INITIALIZED = true;
        }
        worldCamera.getCamera().update();
        avatarCamera.getCamera().update();

        renderShadowSystem.process();
        renderGeometrySystem.process();

        if (DEBUG != null && renderShadowSystem.getDepthMap() != null) {
            onScreenRender.begin();
            onScreenRender.draw(renderShadowSystem.getDepthMap(), 0, 0, 100, 100, 1, 0, 0, 1);
            font.draw(onScreenRender, App.getPlayer().getPositionComponent().toString(), 30, Gdx.graphics.getHeight() - 30);
            font.draw(onScreenRender, String.valueOf(App.getCurrentView()), 30, Gdx.graphics.getHeight() - 60);
            onScreenRender.end();
        }
    }

    private void init() {
        createMaze();
        createCubes();
        createPlayer();
        // createZombies(50);
        addSystemsToWorld();

        onScreenRender = new SpriteBatch();
        server = SERVER != null ? new OnlineServer(world) : new OfflineServer(world);
        world.initialize();

        clock.addService(server);
        font = new BitmapFont();

    }

    /**
     * Generates maze footprint and polygon. Creates maze instance and places it
     * in the game world.
     */
    private void createMaze() {
        mazeMap = new MazeMap(createMap());
    }

    private void createCubes() {

        ArrayList<CubeRegion> cubeRegions = new CubeFactory(mazeMap).getCubeRegions();
        cubeManager = new CubeManager(cubeRegions);
    }

    /**
     * If template is available, creates footprint based on that template,
     * otherwise generates random map.
     * 
     * @return generated map
     */
    private int[][][] createMap() {
        return GeneratorFactory.getInstace(GeneratorType.GENERIC).buildLayered(36, 50);
        // return Assets.map == null ? MazeBuilder.getMaze(MAZE_BLOCKS_COUNT, MAZE_BLOCKS_COUNT) : Assets.map;
        //return MazeBuilder.getLayeredMaze(36, 9);
        // return Assets.map;
    }

    /**
     * Register systems to the world and initialize.
     */
    private void addSystemsToWorld() {
        renderShadowSystem = world.setSystem(new RenderShadowSystem(worldCamera), true);
        renderGeometrySystem = world.setSystem(new RenderGeometrySystem(worldCamera), true);
    }

    /**
     * Place player in the game
     */
    private void createPlayer() {

        Vector2 p = mazeMap.getRandomPosition(10, 10);
        player = new Wanderer(world, p, 1f, 10f, avatarCamera);
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
            Vector2 p = mazeMap.getRandomPosition();
            // Zombie zombie = new Zombie(world, p, 1f, 1f);
            //  zombie.createEntity().addToWorld();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (worldCamera == null) {
            worldCamera = setUpCamera(width, height);
        } else {
            worldCamera.getCamera().viewportWidth = width;
            worldCamera.getCamera().viewportHeight = height;
        }

        if (avatarCamera == null) {
            avatarCamera = setUpCamera(width, height);
        } else {
            avatarCamera.getCamera().viewportWidth = width;
            avatarCamera.getCamera().viewportHeight = height;
        }
    }

    public FollowCameraComponent setUpCamera(int width, int height) {
        PerspectiveCamera camera = new PerspectiveCamera(67, width, height);
        return new FollowCameraComponent(camera, 15f * UNIT, 0);
    }

    // Toggle switches

    public static void toggleWireframe() {
        wireframe = !wireframe;
    }

    public static boolean isWireframe() {
        return wireframe;
    }

    // Global getters

    public static CubeManager getCubeManager() {
        return cubeManager;
    }

    public static Wanderer getPlayer() {
        return player;
    }

    public static MazeMap getMap() {
        return mazeMap;
    }

    public static FollowCameraComponent getWorldCamera() {
        return worldCamera;
    }

    public static FollowCameraComponent getAvatarCamera() {
        return avatarCamera;
    }

    public static AbstractServer getServer() {
        return server;
    }

    public static int getCurrentView() {
        return currentView;
    }

    public static void setCurrentView(int cv) {
        currentView = cv;
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
