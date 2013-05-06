package com.kingx.dungeons.engine.system;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quad;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.WorldRotateComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.tween.CameraAccessor;

public class WorldRotateSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<WorldRotateComponent> worldMapper;
    @Mapper
    ComponentMapper<FollowCameraComponent> cameraMapper;

    public enum State {
        TURNING_LEFT,
        TURNING_RIGHT,
        IDLE;
    }

    private static State currentState = State.IDLE;

    public static State getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(State currentState) {
        WorldRotateSystem.currentState = currentState;
    }

    private final FollowCameraComponent camera;

    private static final float RIGHT_ANGLE = 90f;

    public WorldRotateSystem() {
        super(Aspect.getAspectForAll(WorldRotateComponent.class));
        camera = App.getWorldCamera();
    }

    @Override
    protected void process(Entity e) {
        WorldRotateComponent world = worldMapper.getSafe(e);
        PositionComponent position = world.getPosition();

        float x = Collision.worldToScreen(position.inWorld).x;
        float boundsOffset = App.UNIT - App.PLAYER_OFFSET;
        if (x < CubeRegion.min.x + boundsOffset) {
            rotateLeft(camera, world.getMove());
            Collision.correct(position.get(), App.PLAYER_OFFSET);
        } else if (x > CubeRegion.max.x - boundsOffset) {
            rotateRight(camera, world.getMove());
            Collision.correct(position.get(), App.PLAYER_OFFSET);
        }

        updateCamera(App.getWorldCamera(), position, camera.getAngle() + camera.getArbitratyAngle());
        updateCamera(App.getAvatarCamera(), position, camera.getAngle());
        updateCamera(App.getBackgroundCamera(), position, camera.getAngle());
    }

    private void updateCamera(FollowCameraComponent camera, PositionComponent position, float angle) {
        float y = Math.min(position.getY(), App.getTerrain().getHeight() + 0.5f);
        camera.getCamera().position.x = CubeRegion.mean.x + (float) (Math.sin(angle) * camera.getHeight());
        camera.getCamera().position.y = y;
        camera.getCamera().position.z = CubeRegion.mean.z + (float) (Math.cos(angle) * camera.getHeight());
        camera.getCamera().lookAt(CubeRegion.mean.x, y, CubeRegion.mean.z);
    }

    private void rotateRight(FollowCameraComponent camera, MoveComponent moveComponent) {
        rotateCamera(camera, (float) (Math.PI / 2f));
        moveComponent.addRotation(RIGHT_ANGLE);
        App.getBackgroundManager().hide(App.getCurrentView());
        App.setCurrentView((App.getCurrentView() + 1) % 4);
        App.getBackgroundManager().show(App.getCurrentView());
        WorldRotateSystem.setCurrentState(State.TURNING_RIGHT);
    }

    private void rotateLeft(FollowCameraComponent camera, MoveComponent moveComponent) {
        rotateCamera(camera, (float) (-Math.PI / 2f));
        moveComponent.addRotation(-RIGHT_ANGLE);
        App.getBackgroundManager().hide(App.getCurrentView());
        App.setCurrentView((App.getCurrentView() + 3) % 4);
        App.getBackgroundManager().show(App.getCurrentView());
        WorldRotateSystem.setCurrentState(State.TURNING_LEFT);
    }

    private void rotateCamera(FollowCameraComponent camera, float angle) {
        // We can now create as many interpolations as we need !
        camera.lastAngle += angle;
        Tween.to(camera, CameraAccessor.ROTATION_Y, 1f).target(camera.lastAngle).ease(Quad.OUT).start(App.getTweenManager())
                .setCallback(new TweenCallback() {

                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        setCurrentState(State.IDLE);
                    }
                });
    }
}
