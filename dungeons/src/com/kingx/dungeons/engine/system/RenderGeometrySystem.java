package com.kingx.dungeons.engine.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Shader;

public class RenderGeometrySystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<TextureComponent> tm;
    @Mapper
    ComponentMapper<SizeComponent> ss;
    @Mapper
    ComponentMapper<MoveComponent> moveMapper;
    @Mapper
    ComponentMapper<HealthComponent> healthMapper;

    private final Camera camera;
    private final ShaderProgram shader;
    private final SpriteBatch sb = new SpriteBatch();

    public RenderGeometrySystem(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, TextureComponent.class));
        this.camera = camera;
        this.shader = Shader.getShader("sprite");
        sb.setShader(shader);
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        sb.begin();
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
        sb.end();
    }

    @Override
    protected void process(Entity e) {
        sb.setProjectionMatrix(camera.combined);
        SizeComponent ccs = ss.getSafe(e);
        TextureComponent tc = tm.getSafe(e);

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
            shader.setUniformf("u_tint", tc.getTint());
            PositionComponent pc = pm.getSafe(e);
            sb.draw(currentTexture, pc.getX() - ccs.getSize() / 2f, pc.getY() - ccs.getSize() / 2f, ccs.getSize(), ccs.getSize());
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
