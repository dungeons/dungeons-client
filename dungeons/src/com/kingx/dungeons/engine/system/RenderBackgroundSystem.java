package com.kingx.dungeons.engine.system;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.BackgroundComponent;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.SpriteComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.sprite.SpriteRenderer;
import com.kingx.dungeons.utils.ArrayUtils;

public class RenderBackgroundSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<BackgroundComponent> backgroundMapper;

    private final FollowCameraComponent camera;

    private final SpriteRenderer sr = new SpriteRenderer();

    public RenderBackgroundSystem(FollowCameraComponent camera) {
        super(Aspect.getAspectForAll(BackgroundComponent.class));
        this.camera = camera;
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        sr.begin();
        sr.setProjectionMatrix(camera.getCamera().combined);
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
        sr.end();
    }

    @Override
    protected void process(Entity e) {

        BackgroundComponent background = backgroundMapper.get(e);
        SpriteComponent[] sides = background.getSides();
        switch (WorldRotateSystem.getCurrentState()) {
            case IDLE:
                int side = App.getCurrentView() * 2;
                drawSprite(sides[side]);
                drawSprite(sides[side + 1]);
                break;
            case TURNING_LEFT:
                side = App.getNextView() * 2;
                drawSprite(sides[ArrayUtils.overflow(side + 1, sides.length)]);
                drawSprite(sides[ArrayUtils.overflow(side - 2, sides.length)]);
                drawSprite(sides[ArrayUtils.overflow(side - 1, sides.length)]);
                drawSprite(sides[ArrayUtils.overflow(side, sides.length)]);
                break;
            case TURNING_RIGHT:
                side = App.getPrevView() * 2;
                drawSprite(sides[ArrayUtils.overflow(side, sides.length)]);
                drawSprite(sides[ArrayUtils.overflow(side + 3, sides.length)]);
                drawSprite(sides[ArrayUtils.overflow(side + 2, sides.length)]);
                drawSprite(sides[ArrayUtils.overflow(side + 1, sides.length)]);
                break;
        }

    }

    private void drawSprite(SpriteComponent sprite) {
        TextureComponent t = sprite.getTexture();
        PositionComponent p = sprite.getPositionComponent();
        SizeComponent s = sprite.getSizeComponent();
        t.getTexture().getTexture().bind();
        sr.draw(t.getTexture(), p.get(), s.get(), t.getRotation(), t.getTint());
    }
}
