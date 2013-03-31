package com.kingx.dungeons.engine.system;

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

public class WorldRotateSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<WorldRotateComponent> worldMapper;
    @Mapper
    ComponentMapper<FollowCameraComponent> cameraMapper;

    private static final float RIGHT_ANGLE = 90f;

    public WorldRotateSystem() {
        super(Aspect.getAspectForAll(WorldRotateComponent.class));
    }

    @Override
    protected void process(Entity e) {
        WorldRotateComponent world = worldMapper.getSafe(e);
        FollowCameraComponent camera = cameraMapper.getSafe(e);
        PositionComponent position = world.getPosition();
        SizeComponent size = world.getSize();

        camera = App.getWorldCamera();

        float x = position.getScreenX();
        float boundsOffset = App.UNIT - App.MAP_OFFSET;
        // FIXME making offset smaller it gives player more space and wont trigger collision.
        boundsOffset -= 0.3f;

        if (x < CubeRegion.min.x + boundsOffset) {
            rotateLeft(camera, world.getMove());
            Collision.correct(position, size);
        } else if (x > CubeRegion.max.x - boundsOffset) {
            rotateRight(camera, world.getMove());
            Collision.correct(position, size);
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
        //System.out.println("right");
        camera.angle += Math.PI / 2f;
        moveComponent.addRotation(RIGHT_ANGLE);
        App.setCurrentView((App.getCurrentView() + 1) % 4);
    }

    private void rotateLeft(FollowCameraComponent camera, MoveComponent moveComponent) {
        //68System.out.println("left");
        camera.angle -= Math.PI / 2f;
        moveComponent.addRotation(-RIGHT_ANGLE);
        App.setCurrentView((App.getCurrentView() + 3) % 4);
    }
}
