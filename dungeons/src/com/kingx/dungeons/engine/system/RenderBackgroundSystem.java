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

        //Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
        BackgroundComponent background = backgroundMapper.get(e);

        int view = App.getCurrentView();
        switch (WorldRotateSystem.getCurrentState()) {
            case IDLE:
                drawSprite(background.getSides()[view]);
                break;
            case TURNING_LEFT:
                for (int i = view; i <= view + 1; i++) {
                    drawSprite(background.getSides()[App.getView(i)]);
                }
                drawSprite(background.getSides()[App.getView(view)]);
                break;
            case TURNING_RIGHT:
                for (int i = view; i >= view - 1; i--) {
                    drawSprite(background.getSides()[App.getView(i)]);
                }
                break;
        }
        //   Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

    }

    private void drawSprite(SpriteComponent sprite) {
        System.out.println("Drawign: " + sprite);
        TextureComponent t = sprite.getTexture();
        PositionComponent p = sprite.getPositionComponent();
        SizeComponent s = sprite.getSizeComponent();
        t.getTexture().getTexture().bind();
        sr.draw(t.getTexture(), p.get(), s.get(), t.getRotation(), t.getTint());
    }
}
