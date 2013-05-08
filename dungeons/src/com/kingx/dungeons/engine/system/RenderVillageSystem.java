package com.kingx.dungeons.engine.system;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.VillageComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;
import com.kingx.dungeons.graphics.cube.CubeRenderer;

public class RenderVillageSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<VillageComponent> villageMapper;
    @Mapper
    ComponentMapper<MeshComponent> meshMapper;
    @Mapper
    ComponentMapper<TextureComponent> textureMapper;
    @Mapper
    ComponentMapper<PositionComponent> positionMapper;

    private final FollowCameraComponent camera;

    private final ShaderProgram villageShader;
    private final ShaderProgram starShader;
    private TextureRegion currentTexture;
    private CubeRenderer batchRender;

    public RenderVillageSystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(VillageComponent.class));
        this.camera = camera;
        villageShader = Shader.getShader("house");
        starShader = Shader.getShader("star");
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        if (batchRender == null) {
            batchRender = new CubeRenderer();
        }
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {

    }

    @Override
    protected void process(Entity e) {
        TextureComponent texture = textureMapper.get(e);
        if (currentTexture == null || currentTexture != texture.getTexture()) {
            currentTexture = texture.getTexture();
            currentTexture.getTexture().bind();
        }

        MeshComponent mesh = meshMapper.get(e);
        PositionComponent position = positionMapper.get(e);

        villageShader.begin();
        Vector3 playerPosition = App.getPlayer().getPositionComponent().get();
        villageShader.setUniformMatrix("u_projTrans", camera.camera.combined);
        villageShader.setUniformMatrix("u_viewTrans", camera.camera.view);
        villageShader.setUniformf("u_lightSourcePos", playerPosition);

        // Without w argument. Uniform will not be mapped to vec4.
        villageShader.setUniformf("u_positionOffset", position.get());
        villageShader.setUniformf("u_source_color", Colors.WALL_LIGHT);
        villageShader.setUniformf("u_ground_color", Colors.WALL_SHADOW);
        villageShader.setUniformf("u_sight", App.getPlayer().getEntity().getComponent(SightComponent.class).getRadius());
        mesh.getMesh().render(villageShader, GL10.GL_TRIANGLES);

        villageShader.end();

    }
}
