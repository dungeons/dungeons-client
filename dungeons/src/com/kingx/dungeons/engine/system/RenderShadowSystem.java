package com.kingx.dungeons.engine.system;

import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.entity.graphics.Colors;
import com.kingx.dungeons.entity.graphics.QuadTextureFrameBuffer;
import com.kingx.dungeons.entity.graphics.Shader;

public class RenderShadowSystem extends EntitySystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<MeshComponent> mm;
    @Mapper
    ComponentMapper<ShadowComponent> sm;

    private final Camera camera;

    private Bag<AtlasRegion> regionsByEntity;
    private List<Entity> sortedEntities;
    private boolean begin;
    private final ShaderProgram groundShader;
    private final ShaderProgram shadowGeneratorShader;
    private final ShaderProgram shadowProjectShader;
    private final QuadTextureFrameBuffer shadowMap;
    private final Mesh poly;
    private final float BOUNDS = 500f;
    private Texture depthMap;

    public RenderShadowSystem(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, MeshComponent.class, ShadowComponent.class));
        this.camera = camera;

        float[] outVerts = new float[] { -BOUNDS, -BOUNDS, 0, BOUNDS, -BOUNDS, 0, BOUNDS, BOUNDS, 0, -BOUNDS, BOUNDS, 0 };
        short[] outIndices = new short[] { 1, 2, 0, 3 };
        poly = new Mesh(true, outVerts.length, outIndices.length, VertexAttribute.Position());
        poly.setVertices(outVerts);
        poly.setIndices(outIndices);

        groundShader = Shader.getShader("ground");
        shadowGeneratorShader = Shader.getShader("shadowgen");
        shadowProjectShader = Shader.getShader("shadowproj");

        shadowMap = new QuadTextureFrameBuffer(Format.RGBA8888, 1024, 1024, true);

    }

    @Override
    protected void begin() {
        begin = true;
    }

    @Override
    protected void end() {
        begin = false;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        for (int i = 0; entities.size() > i; i++) {
            process(entities.get(i));
        }
    }

    protected void process(Entity e) {
        if (begin) {
            System.out.println("RenderShadowSystem");
            PositionComponent pc = pm.getSafe(e);
            ShadowComponent sc = sm.getSafe(e);
            MeshComponent mc = mm.getSafe(e);

            /*  camera.combined.translate(pc.x, pc.y, pc.z);

              sc.shader.begin();
              sc.shader.setUniformMatrix("u_MVPMatrix", camera.combined);
              mc.mesh.render(sc.shader, GL10.GL_TRIANGLES);
              sc.shader.end();

              camera.combined.translate(-pc.x, -pc.y, -pc.z);
            */

            Camera[] lights = sc.lights;
            for (Camera light : lights) {
                light.position.x = pc.x;
                light.position.y = pc.y;
            }
            generateShadowMap(lights);

            // System.out.println(sce);
            depthMap.bind();

            shadowProjectShader.begin();
            shadowProjectShader.setUniformMatrix("ProjectionMatrix", camera.projection);
            shadowProjectShader.setUniformMatrix("ViewMatrix", camera.view);
            for (int i = 0; i < lights.length; i++) {
                shadowProjectShader.setUniformMatrix("LightSourceProjectionViewMatrix[" + i + "]", lights[i].combined);
            }
            shadowProjectShader.setUniformf("v_lightSpacePosition", lights[0].position);
            shadowProjectShader.setUniformf("u_source_color", Colors.SHADOW.color);
            shadowProjectShader.setUniformf("u_ground_color", Colors.GROUND.color);
            shadowProjectShader.setUniformi("DepthMap", 0);
            // shadowProjectShader.setUniformf("color", 0.4f, 0.6f, 0.7f, 1f);

            poly.render(shadowProjectShader, GL20.GL_TRIANGLE_STRIP);
            shadowProjectShader.end();

            /*  groundShader.begin();
              groundShader.setUniformMatrix("PVMMatrix", cam.combined);
              groundShader.setUniformf("u_mazeSize", App.MAZE_BLOCKS_COUNT * App.MAZE_WALL_SIZE);
              poly.render(groundShader, GL20.GL_TRIANGLE_STRIP);
              groundShader.end();*/

        }

    }

    private void generateShadowMap(Camera[] lights) {
        shadowMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        for (int i = 0; i < lights.length; i++) {
            lights[i].update();
            depthMap = generateShadowMap(lights[i]);
        }

        shadowMap.end();
    }

    private Texture generateShadowMap(Camera lightCam) {
        shadowMap.nextTexture();
        shadowGeneratorShader.begin();
        shadowGeneratorShader.setUniformMatrix("ProjectionMatrix", lightCam.projection);
        shadowGeneratorShader.setUniformMatrix("ViewMatrix", lightCam.view);
        App.getMaze().render(shadowGeneratorShader, GL20.GL_TRIANGLES);
        shadowGeneratorShader.end();

        return shadowMap.getColorBufferTexture();
    }

    public Texture getCbt() {
        return depthMap;
    }

}
