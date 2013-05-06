package com.kingx.dungeons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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
import com.kingx.dungeons.GameStateManager.GameStatus;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.concrete.Building;
import com.kingx.dungeons.engine.concrete.Wanderer;
import com.kingx.dungeons.engine.system.RenderBackgroundSystem;
import com.kingx.dungeons.engine.system.RenderGeometrySystem;
import com.kingx.dungeons.engine.system.RenderMineralSystem;
import com.kingx.dungeons.engine.system.RenderPlainSystem;
import com.kingx.dungeons.engine.system.RenderShadowSystem;
import com.kingx.dungeons.engine.system.RenderVillageSystem;
import com.kingx.dungeons.generator.GeneratorFactory;
import com.kingx.dungeons.generator.GeneratorType;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Terrain;
import com.kingx.dungeons.graphics.cube.Cube;
import com.kingx.dungeons.graphics.cube.CubeBlockSideFactory;
import com.kingx.dungeons.graphics.cube.CubeManager;
import com.kingx.dungeons.graphics.cube.CubeMineralSideFactory;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.graphics.cube.CubeSkyFactory;
import com.kingx.dungeons.graphics.cube.CubeTopFactory;
import com.kingx.dungeons.graphics.cube.SimpleCube;
import com.kingx.dungeons.graphics.ui.Gamepad;
import com.kingx.dungeons.input.Input;
import com.kingx.dungeons.server.AbstractServer;
import com.kingx.dungeons.server.OfflineServer;
import com.kingx.dungeons.server.OnlineServer;
import com.kingx.dungeons.tween.BackgroundAccessor;
import com.kingx.dungeons.tween.CameraAccessor;
import com.kingx.dungeons.tween.CubeAccessor;
import com.kingx.dungeons.tween.Vector3Accessor;

public class App implements ApplicationListener {

    // Global parameters
    public static Param DEBUG;
    public static Param NOSLEEP;
    public static Param SERVER;
    public static Param REPLAY;

    public static Random rand;

    private static TweenManager tweenManager = new TweenManager();
    static {
        Tween.registerAccessor(FollowCameraComponent.class, new CameraAccessor());
        Tween.registerAccessor(TextureComponent.class, new BackgroundAccessor());
        Tween.registerAccessor(Vector3.class, new Vector3Accessor());
        Tween.registerAccessor(Cube.class, new CubeAccessor());
    }
    private static FollowCameraComponent worldCamera;
    private static FollowCameraComponent avatarCamera;
    private static FollowCameraComponent backgroundCamera;

    private static Terrain terrain;
    private static CubeManager cubeManager;

    public static final float UNIT = 1f;
    public static final float VIEW_DISTANCE = 12f;
    public static final float PLAYER_OFFSET = 0.5f;

    private static boolean wireframe;
    private static float progress;

    private final Map<String, Param> params;
    private static Clock clock;
    private GameStateManager state;
    private static Input input;

    public App(String[] args) {
        params = args != null ? Param.getParams(args) : Param.getParams(new String[] {});
        DEBUG = getParam("-d", "-debug");
        NOSLEEP = getParam("-ns", "-nosleep");
        SERVER = getParam("-s", "-server");
        REPLAY = getParam("-r", "-replay");

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

        state = REPLAY != null ? GameStateManager.getInstance(GameStatus.REPLAY) : GameStateManager.getInstance(GameStatus.RECORD);
        rand = new Random(state.getSeed());

        clock = new Clock();
        input = new Input();
        Gdx.input.setInputProcessor(input);

        ShaderProgram.pedantic = false;
        world = new World();

        Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL10.GL_LESS);

    }

    public static boolean INITIALIZED = false;
    private SpriteBatch onScreenRasterRender;
    private ShapeRenderer onScreenVectorRender;
    private World world;
    private RenderPlainSystem renderPlainSystem;
    private RenderShadowSystem renderShadowSystem;
    private RenderMineralSystem renderMineralSystem;
    private RenderGeometrySystem renderGeometrySystem;
    private RenderVillageSystem renderVillageSystem;
    private RenderBackgroundSystem renderBackgroundSystem;
    private static Wanderer player;
    private static AbstractServer server;
    private static int currentView;
    private static int lastView;
    BitmapFont font = null;
    private TerrainManager mazeManager;
    private Gamepad ui;
    public static List<SimpleCube> sky;
    private static BackgroundManager backgroundManager;
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
        backgroundCamera.getCamera().update();

        renderBackgroundSystem.process();
        renderVillageSystem.process();
        renderShadowSystem.process();
        renderPlainSystem.process();
        renderMineralSystem.process();
        renderGeometrySystem.process();

        if (DEBUG != null) {
            onScreenRasterRender.begin();
            if (renderShadowSystem.getDepthMap() != null) {
                onScreenRasterRender.draw(renderShadowSystem.getDepthMap(), 0, 0, 100, 100, 1, 0, 0, 1);
            }
            font.draw(onScreenRasterRender, App.getPlayer().getPositionComponent().toString(), 30, Gdx.graphics.getHeight() - 30);
            font.draw(onScreenRasterRender, String.valueOf(App.getCurrentView()), 30, Gdx.graphics.getHeight() - 60);
            font.draw(onScreenRasterRender, "GPU:" + String.valueOf(Gdx.graphics.getFramesPerSecond()), 30, Gdx.graphics.getHeight() - 90);
            font.draw(onScreenRasterRender, "CPU:" + String.valueOf(Math.round(clock.getFPS())), 30, Gdx.graphics.getHeight() - 120);
            font.draw(onScreenRasterRender, "Clocks:" + String.valueOf(clock.getClocks()), 30, Gdx.graphics.getHeight() - 150);
            onScreenRasterRender.end();
        }
        if (ui != null) {
            ui.render();
        }

        Gdx.gl.glClearColor(tint.r, tint.g, tint.b, 1);

    }

    private void init() {
        createMaze();
        createVillage();
        createBackground();
        createCubes();
        createPlayer();
        createSky();
        // createZombies(50);
        addSystemsToWorld();

        onScreenRasterRender = new SpriteBatch();
        onScreenVectorRender = new ShapeRenderer();
        server = SERVER != null ? new OnlineServer(world) : new OfflineServer(world, state);
        world.initialize();

        clock.addService(server);
        font = new BitmapFont();

        if (DEBUG != null || Gdx.app.getType() != ApplicationType.Desktop) {
            ui = new Gamepad(onScreenVectorRender);
            gamepad = ui;
        }

    }

    private void createSky() {

        sky = new CubeSkyFactory(5, 50, -5, 0.1f, 10, 15f).getCubes();

    }

    private void createVillage() {
        try {
            createBuilding(4.8f, 1.2f, "mine", 1f);
            createBuilding(2.7f, 0.7f, "tree", 0.5f);
            createBuilding(7.5f, 3, "tree", 1.5f);
            createBuilding(5, 5.5f, "tree", 2f);
            createBuilding(5, 3, "tree", 2.5f);
            createBuilding(2, 6, "tree", 1f);
            createBuilding(2, 3, "tree", 1f);

        } catch (IOException e) {
            e.printStackTrace();
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

        ArrayList<CubeRegion> cubeBlockSides = new CubeBlockSideFactory(terrain).getCubeRegions();
        ArrayList<CubeRegion> cubeMineralSides = new CubeMineralSideFactory(terrain).getCubeRegions();
        CubeRegion cubeTop = new CubeTopFactory(terrain.getWidth() - 2, 1, terrain.getHeight() - 1, -1).getCubeRegions();
        cubeManager = new CubeManager(cubeTop, cubeBlockSides, cubeMineralSides);
    }

    /**
     * If template is available, creates footprint based on that template,
     * otherwise generates random map.
     * 
     * @return generated map
     */
    private BlockPair[][][] createMap() {
        return GeneratorFactory.getInstace(GeneratorType.GENERIC).buildLayered(36, 50);
        // return Assets.map == null ? MazeBuilder.getMaze(MAZE_BLOCKS_COUNT, MAZE_BLOCKS_COUNT) : Assets.map;
        //return MazeBuilder.getLayeredMaze(36, 9);
        // return Assets.map;
    }

    /**
     * Register systems to the world and initialize.
     */
    private void addSystemsToWorld() {
        renderPlainSystem = world.setSystem(new RenderPlainSystem(worldCamera, cubeManager.getTop()), true);
        renderShadowSystem = world.setSystem(new RenderShadowSystem(worldCamera, cubeManager.getBlockSides()), true);
        renderMineralSystem = world.setSystem(new RenderMineralSystem(worldCamera, cubeManager.getMineralSides()), true);
        renderGeometrySystem = world.setSystem(new RenderGeometrySystem(worldCamera), true);
        renderVillageSystem = world.setSystem(new RenderVillageSystem(worldCamera), true);
        renderBackgroundSystem = world.setSystem(new RenderBackgroundSystem(backgroundCamera), true);
    }

    private void createBackground() {
        backgroundManager = new BackgroundManager(world, terrain.getWidth(), terrain.getHeight());
        // backgroundManager.addBackground(1, -2, "well");
        //  backgroundManager.addBackground(3, -5, "house");
        //  backgroundManager.addBackground(4, -1, "well");
        //  backgroundManager.addBackground(9, -5, "house");
    }

    /**
     * Place player in the game
     */
    private void createPlayer() {

        Vector2 p = new Vector2(App.rand.nextInt(10), App.getTerrain().getHeight() + 3);
        player = new Wanderer(world, p, 1f, 5f, avatarCamera);
        player.createEntity().addToWorld();
    }

    /**
     * Place player in the game
     * 
     * @throws IOException
     */
    private void createBuilding(float x, float y, String name, float scale) throws IOException {
        createObject(x, App.getTerrain().getHeight(), -y, name, scale);
    }

    private void createObject(float x, float y, float z, String name, float scale) throws IOException {

        Building building = new Building(world, new Vector3(x, y, z), name, scale);
        building.createEntity().addToWorld();
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
            worldCamera = setUpPerspectiveCamera(width, height);
        } else {
            worldCamera.getCamera().viewportWidth = width;
            worldCamera.getCamera().viewportHeight = height;
        }

        if (avatarCamera == null) {
            avatarCamera = setUpPerspectiveCamera(width, height);
        } else {
            avatarCamera.getCamera().viewportWidth = width;
            avatarCamera.getCamera().viewportHeight = height;
        }

        if (backgroundCamera == null) {
            backgroundCamera = setUpPerspectiveCamera(width, height);
        } else {
            backgroundCamera.getCamera().viewportWidth = width;
            backgroundCamera.getCamera().viewportHeight = height;
        }
    }

    public FollowCameraComponent setUpPerspectiveCamera(int width, int height) {
        Camera camera = new PerspectiveCamera(67, width, height);
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

    public static Clock getClock() {
        return clock;
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

    public static Terrain getTerrain() {
        return terrain;
    }

    public static BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    public static FollowCameraComponent getWorldCamera() {
        return worldCamera;
    }

    public static FollowCameraComponent getAvatarCamera() {
        return avatarCamera;
    }

    public static FollowCameraComponent getBackgroundCamera() {
        return backgroundCamera;
    }

    public static AbstractServer getServer() {
        return server;
    }

    public static int getCurrentView() {
        return currentView;
    }

    public static int getPrevView() {
        return (currentView == 0 ? App.getCubeManager().cubeRegionPacks.size() : currentView) - 1;
    }

    public static int getNextView() {
        return (currentView + 1) % App.getCubeManager().cubeRegionPacks.size();
    }

    public static int getLastView() {
        return lastView;
    }

    public static int getView(int i) {
        if (i < 0) {
            return i + App.getCubeManager().cubeRegionPacks.size();
        } else if (i >= App.getCubeManager().cubeRegionPacks.size()) {
            return i - App.getCubeManager().cubeRegionPacks.size();
        } else {
            return i;
        }
    }

    public static void setCurrentView(int cv) {
        lastView = currentView;
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
        state.writeState();
    }

    public static Input getInput() {
        return input;
    }

}
