package com.kingx.dungeons.engine.system;

import aurelienribon.tweenengine.Tween;
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
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.graphics.cube.CubeRegion;
import com.kingx.dungeons.tween.CameraAccessor;

public class WorldRotateSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<WorldRotateComponent> worldMapper;
    @Mapper
    ComponentMapper<FollowCameraComponent> cameraMapper;
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
        SizeComponent size = world.getSize();

        float x = position.getScreenX();
        float boundsOffset = App.UNIT - App.PLAYER_OFFSET;
        // FIXME making offset smaller it gives player more space and wont trigger collision.
        //  boundsOffset -= 0.3f;

        if (x < CubeRegion.min.x + boundsOffset) {
            rotateLeft(camera, world.getMove());
            Collision.correct(position.get(), App.PLAYER_OFFSET);
        } else if (x > CubeRegion.max.x - boundsOffset) {
            rotateRight(camera, world.getMove());
            Collision.correct(position.get(), App.PLAYER_OFFSET);
        }

        updateCamera(App.getWorldCamera(), position, camera.getAngle() + camera.getArbitratyAngle());
        updateCamera(App.getAvatarCamera(), position, camera.getAngle());

    }

    private void updateCamera(FollowCameraComponent camera, PositionComponent position, float angle) {
        camera.getCamera().position.x = CubeRegion.mean.x + (float) (Math.sin(angle) * camera.getHeight());
        camera.getCamera().position.y = position.getY();
        camera.getCamera().position.z = CubeRegion.mean.z + (float) (Math.cos(angle) * camera.getHeight());
        camera.getCamera().lookAt(CubeRegion.mean.x, position.getY(), CubeRegion.mean.z);
    }

    private void rotateRight(FollowCameraComponent camera, MoveComponent moveComponent) {
        rotateCamera(camera, (float) (Math.PI / 2f));
        moveComponent.addRotation(RIGHT_ANGLE);
        App.setCurrentView((App.getCurrentView() + 1) % 4);
    }

    private void rotateLeft(FollowCameraComponent camera, MoveComponent moveComponent) {
        rotateCamera(camera, (float) (-Math.PI / 2f));
        moveComponent.addRotation(-RIGHT_ANGLE);
        App.setCurrentView((App.getCurrentView() + 3) % 4);
    }

    private void rotateCamera(FollowCameraComponent camera, float angle) {

        // We can now create as many interpolations as we need !
        camera.lastAngle += angle;
        Tween.to(camera, CameraAccessor.ROTATION_Y, 1.0f).target(camera.lastAngle).ease(Quad.OUT).start(App.getTweenManager());
    }
}
