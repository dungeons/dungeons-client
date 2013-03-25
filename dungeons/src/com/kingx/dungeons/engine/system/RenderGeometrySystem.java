package com.kingx.dungeons.engine.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.geom.Polygon;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class RenderGeometrySystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> positionMapper;
    @Mapper
    ComponentMapper<TextureComponent> textureMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;
    @Mapper
    ComponentMapper<MoveComponent> moveMapper;
    @Mapper
    ComponentMapper<HealthComponent> healthMapper;
    @Mapper
    ComponentMapper<SightComponent> sightMapper;

    private final Camera camera;
    private final ShaderProgram shader;
    private PositionComponent playerPosition;
    private SightComponent playerSight;
    private Mesh mesh;

    public RenderGeometrySystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, TextureComponent.class));
        this.camera = camera.getCamera();
        this.shader = Shader.getShader("sprite");
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        playerPosition = positionMapper.getSafe(App.getPlayer().getEntity());
        playerSight = sightMapper.getSafe(App.getPlayer().getEntity());
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
    }

    @Override
    protected void process(Entity e) {

        SizeComponent ccs = sizeMapper.getSafe(e);
        TextureComponent tc = textureMapper.getSafe(e);
        PositionComponent pc = positionMapper.getSafe(e);
        if (!Collision.canSee(pc.inWorld, playerPosition.inWorld, playerSight.getRadius())) {
            return;
        }

        TextureRegion currentTexture = null;

        if (healthMapper.has(e)) {
            HealthComponent health = healthMapper.get(e);
            String name = health.getCurrent() < health.getMax() / 2 ? tc.getDamaged() : tc.getHealty();

            if (moveMapper.has(e)) {
                MoveComponent moveComponent = moveMapper.get(e);
                currentTexture = getRightTexture(moveComponent.vector, name);
            } else {
                currentTexture = Assets.getTexture(name, 0);
            }

        }

        if (currentTexture != null) {

            mesh = Polygon.createPlain(0.5f, currentTexture.getU(), currentTexture.getV(), currentTexture.getU2(), currentTexture.getV2());

            currentTexture.getTexture().bind();
            shader.begin();
            camera.translate(pc.inWorld.cpy().mul(-1));
            camera.update();
            shader.setUniformMatrix("u_projTrans", camera.combined);
            camera.translate(pc.inWorld);
            camera.update();
            shader.setUniformi("sampler2D", 0);
            shader.setUniformf("u_tint", Colors.ZOMBIE_ALARM);
            mesh.render(shader, GL20.GL_TRIANGLE_STRIP);

            shader.begin();

            // sb.setProjectionMatrix(camera.combined);
            //  sb.draw(currentTexture, pc.getX() - ccs.getSize() / 2f, pc.getY() - ccs.getSize() / 2f, ccs.getSize(), ccs.getSize());

        }
    }

    public static TextureRegion getRightTexture(Vector3 vector, String name) {
        int ang = (int) Math.toDegrees(Math.atan2(vector.y, vector.x)) + 90 + 22;
        if (ang < 0) {
            ang += 360;
        } else if (ang > 360) {
            ang -= 360;
        }
        ang = (int) (ang / 360f * 8);
        return Assets.getTexture(name, ang);
    }
}
