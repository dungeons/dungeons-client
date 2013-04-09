package com.kingx.dungeons;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.concrete.Background;
import com.kingx.dungeons.engine.concrete.Wanderer;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.engine.system.RenderShadowSystem;
import com.kingx.dungeons.generator.GeneratorFactory;
import com.kingx.dungeons.generator.GeneratorType;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Terrain;
import com.kingx.dungeons.graphics.cube.CubeFactory;
import com.kingx.dungeons.graphics.cube.CubeManager;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.graphics.ui.Gamepad;
import com.kingx.dungeons.input.Input;
import com.kingx.dungeons.server.AbstractServer;
import com.kingx.dungeons.server.OfflineServer;
import com.kingx.dungeons.server.OnlineServer;
import com.kingx.dungeons.tween.CameraAccessor;

public class App implements ApplicationListener {

    // Global parameters
    public static Param DEBUG;
    public static Param NOSLEEP;
    public static Param SERVER;

    public static final Random rand = new Random();
    private static TweenManager tweenManager = new TweenManager();
    static {
        Tween.registerAccessor(FollowCameraComponent.class, new CameraAccessor());
    }
    private static FollowCameraComponent worldCamera;
    private static FollowCameraComponent avatarCamera;

    private static Terrain terrain;
    private static CubeManager cubeManager;

    public static final float UNIT = 1f;
    public static final float VIEW_DISTANCE = 12f;
    public static final float PLAYER_OFFSET = 0.5f;
    public static final float LIGHT_OFFSET = 0.1f;

    private static boolean wireframe;
    private static float progress;

    private final Map<String, Param> params;
    private Clock clock;
    private static Input input;

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
        input = new Input();
        Gdx.input.setInputProcessor(input);
    }

    public static boolean INITIALIZED = false;
    private SpriteBatch onScreenRasterRender;
    private ShapeRenderer onScreenVectorRender;
    private World world;
    private RenderShadowSystem renderShadowSystem;
    private RenderGeometrySystem renderGeometrySystem;
    private static Wanderer player;
    private static AbstractServer server;
    private static int currentView;
    private static int lastView;
    BitmapFont font = null;
    private TerrainManager mazeManager;
    private Gamepad ui;
    private static Gamepad gamepad;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        if (!INITIALIZED) {
            init();
            INITIALIZED = true;
        }
        progress = player.getPositionComponent().getY() / terrain.getHeight();
        Color tint = Colors.interpolate(Colors.WORLD_BOTTOM, Colors.SKY, progress, 1);

        worldCamera.getCamera().update();
        avatarCamera.getCamera().update();

        renderShadowSystem.process();
        // renderGeometrySystem.process();

        if (DEBUG != null && renderShadowSystem.getDepthMap() != null) {
            onScreenRasterRender.begin();
            onScreenRasterRender.draw(renderShadowSystem.getDepthMap(), 0, 0, 100, 100, 1, 0, 0, 1);
            font.draw(onScreenRasterRender, App.getPlayer().getPositionComponent().toString(), 30, Gdx.graphics.getHeight() - 30);
            font.draw(onScreenRasterRender, String.valueOf(App.getCurrentView()), 30, Gdx.graphics.getHeight() - 60);
            onScreenRasterRender.end();
        }
        if (ui != null) {
            ui.render();
        }
        Gdx.gl.glClearColor(tint.r, tint.g, tint.b, tint.a);

    }

    private void init() {
        createMaze();
        createBackground(0);
        createCubes();
        createPlayer();
        // createZombies(50);
        addSystemsToWorld();

        onScreenRasterRender = new SpriteBatch();
        onScreenVectorRender = new ShapeRenderer();
        server = SERVER != null ? new OnlineServer(world) : new OfflineServer(world);
        world.initialize();

        clock.addService(server);
        font = new BitmapFont();

        if (DEBUG != null || Gdx.app.getType() != ApplicationType.Desktop) {
            ui = new Gamepad(onScreenVectorRender);
            gamepad = ui;
        }

    }

    /**
     * Generates maze footprint and polygon. Creates maze instance and places it
     * in the game world.
     */
    private void createMaze() {
        terrain = new Terrain(createMap());
        mazeManager = new TerrainManager(terrain);
    }

    private void createCubes() {

        ArrayList<CubeRegion> cubeRegions = new CubeFactory(terrain).getCubeRegions();
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

    private void createBackground(float z) {

        int height = terrain.getHeight();
        Vector3 p = new Vector3(5, height + 2f, z);
        new Background(world, p, 10, 4f).createEntity().addToWorld();
    }

    /**
     * Place player in the game
     */
    private void createPlayer() {

        Vector2 p = terrain.getRandomPosition(5, 5);
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
            Vector2 p = terrain.getRandomPosition();
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
        return new FollowCameraComponent(camera, VIEW_DISTANCE * UNIT, 0);
    }

    // Toggle switches

    public static void toggleWireframe() {
        wireframe = !wireframe;
    }

    public static boolean isWireframe() {
        return wireframe;
    }

    // Global getters

    public static Gamepad getGamepad() {
        return gamepad;
    }

    public static CubeManager getCubeManager() {
        return cubeManager;
    }

    public static TweenManager getTweenManager() {
        return tweenManager;
    }

    public static float getProgress() {
        return progress;
    }

    public static Wanderer getPlayer() {
        return player;
    }

    public static Terrain getMap() {
        return terrain;
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

    public static int getPrevView() {
        return (currentView == 0 ? App.getCubeManager().cubeRegions.size() : currentView) - 1;
    }

    public static int getNextView() {
        return (currentView + 1) % App.getCubeManager().cubeRegions.size();
    }

    public static int getLastView() {
        return lastView;
    }

    public static void setCurrentView(int cv) {
        lastView = currentView;
        currentView = cv;

        System.out.println(cubeManager.getCubeRegions().get(lastView));
        System.out.println();
        System.out.println(cubeManager.getCubeRegions().get(currentView));
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

    public static Input getInput() {
        return input;
    }

}
