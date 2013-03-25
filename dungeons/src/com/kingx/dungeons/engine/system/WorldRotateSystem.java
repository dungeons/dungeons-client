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

public class WorldRotateSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<WorldRotateComponent> worldMapper;
    @Mapper
    ComponentMapper<FollowCameraComponent> cameraMapper;

    public WorldRotateSystem() {
        super(Aspect.getAspectForAll(WorldRotateComponent.class));
    }

    @Override
    protected void process(Entity e) {
        WorldRotateComponent world = worldMapper.getSafe(e);
        FollowCameraComponent camera = cameraMapper.getSafe(e);

        PositionComponent position = world.getPosition();
        SizeComponent size = world.getSize();
        camera.getCamera().position.x = 5;
        camera.getCamera().position.y = position.getY();
        camera.getCamera().position.z = 10f;
        camera.getCamera().lookAt(5, position.getY(), -5);

        camera = App.getWorldCamera();
        float x = position.getScreenX();
        if (x < size.getSize() / 2) {
            rotateLeft(camera, world.getMove());
            Collision.correct(position, size);
        } else if (x > App.getMap().getWidth() - size.getSize() / 2) {
            rotateRight(camera, world.getMove());
            Collision.correct(position, size);
        }

        camera.getCamera().position.x = 5 + (float) (Math.sin(camera.angle + camera.arbitratyAngle) * camera.getHeight());
        camera.getCamera().position.y = position.getY();
        camera.getCamera().position.z = -5 + (float) (Math.cos(camera.angle + camera.arbitratyAngle) * camera.getHeight());
        camera.getCamera().lookAt(5, position.getY(), -5);

        camera = App.getAvatarCamera();
        camera.getCamera().position.x = 5 + (float) (Math.sin(camera.angle) * camera.getHeight());
        camera.getCamera().position.y = position.getY();
        camera.getCamera().position.z = -5 + (float) (Math.cos(camera.angle) * camera.getHeight());
        camera.getCamera().lookAt(5, position.getY(), -5);

    }

    private void rotateRight(FollowCameraComponent camera, MoveComponent moveComponent) {
        System.out.println("right " + (App.getCurrentView() + 1) % 4);
        //    camera.angle += Math.PI / 2f;
        moveComponent.addRotation(90f);
        App.setCurrentView((App.getCurrentView() + 1) % 4);
    }

    private void rotateLeft(FollowCameraComponent camera, MoveComponent moveComponent) {
        System.out.println("left " + (App.getCurrentView() + 3) % 4);
        //      camera.angle -= Math.PI / 2f;
        moveComponent.addRotation(-90f);
        App.setCurrentView((App.getCurrentView() + 3) % 4);
    }
}
