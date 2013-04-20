package com.kingx.dungeons.engine.system;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.VillageComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
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
        shader = Shader.getShader("normal");
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

        camera.camera.translate(-position.getX(), -position.getY(), -position.getZ());
        camera.camera.update();

        shader.setUniformMatrix("u_projTrans", camera.camera.combined);
        mesh.getMesh().render(shader, GL10.GL_TRIANGLES);

        camera.camera.translate(position.getX(), position.getY(), position.getZ());
        camera.camera.update();
    }
}
