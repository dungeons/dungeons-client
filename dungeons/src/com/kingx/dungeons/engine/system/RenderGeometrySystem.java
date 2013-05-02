package com.kingx.dungeons.engine.system;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.kingx.dungeons.graphics.Shader;
import com.kingx.dungeons.graphics.sprite.SpriteRenderer;

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

    private final FollowCameraComponent camera;
    private PositionComponent playerPosition;
    private SightComponent playerSight;

    private final SpriteRenderer sr = new SpriteRenderer();

    public RenderGeometrySystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, MoveComponent.class));
        this.camera = camera;
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        playerPosition = positionMapper.getSafe(App.getPlayer().getEntity());
        playerSight = sightMapper.getSafe(App.getPlayer().getEntity());
        sr.begin();
        sr.setShader(Shader.getShader("sprite"));
        sr.setProjectionMatrix(camera.getCamera().combined);
        sr.enableBlending();
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
        sr.disableBlending();
        sr.end();
    }

    @Override
    protected void process(Entity e) {

        MoveComponent move = moveMapper.getSafe(e);
        TextureComponent tc = textureMapper.getSafe(e);
        PositionComponent pc = positionMapper.getSafe(e);
        SizeComponent sc = sizeMapper.getSafe(e);
        /*  if (!Collision.canSee(pc.inWorld, playerPosition.inWorld, playerSight.getRadius())) {
              return;
          }*/

        TextureRegion currentTexture = null;
        /* String name;

         if (healthMapper.has(e)) {
             HealthComponent health = healthMapper.get(e);
             name = health.getCurrent() < health.getMax() / 2 ? tc.getDamaged() : tc.getHealty();

         } else {
             name = tc.getHealty();
         }

         float rotation = 0;
         if (moveMapper.has(e)) {
             System.out.println(move.vector);

             rotation = (float) (Math.atan2(move.vector.y, move.vector.x) / Math.PI * 180);
         }
         currentTexture = getRightTexture(rotation, name);
        */
        currentTexture = tc.getTexture();
        if (currentTexture != null) {
            currentTexture.getTexture().bind();
            sr.draw(currentTexture, pc.get(), sc.get(), 0);

        }
    }

    public static TextureRegion getRightTexture(float rotation, String name) {
        int ang = (int) rotation % 360;
        if (ang < 0)
            ang += 360;
        ang = (int) (ang / 360f * 8);
        return Assets.getTexture(name, ang);
    }
}
