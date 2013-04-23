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

    private final ShaderProgram shader;
    private TextureRegion currentTexture;

    public RenderVillageSystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(VillageComponent.class));
        this.camera = camera;
        shader = Shader.getShader("house");
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        shader.begin();
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
        shader.end();

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

        Vector3 playerPosition = App.getPlayer().getPositionComponent().get();
        shader.setUniformMatrix("u_projTrans", camera.camera.combined);
        shader.setUniformMatrix("u_viewTrans", camera.camera.view);
        shader.setUniformf("u_lightSourcePos", playerPosition);

        // Without w argument. Uniform will not be mapped to vec4.
        shader.setUniformf("u_positionOffset", position.get());
        shader.setUniformf("u_source_color", Colors.WALL_LIGHT);
        shader.setUniformf("u_ground_color", Colors.WALL_SHADOW);
        shader.setUniformf("u_sight", App.getPlayer().getEntity().getComponent(SightComponent.class).getRadius());
        mesh.getMesh().render(shader, GL10.GL_TRIANGLES);

    }
}
