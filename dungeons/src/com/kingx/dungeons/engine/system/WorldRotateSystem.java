package com.kingx.dungeons.engine.system;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.FollowCameraComponent;
import com.kingx.dungeons.engine.component.WorldRotateComponent;
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
        float x = Collision.converX(position);
        if (x < size.getSize() / 2) {
            System.out.println(position.vector.x);
            rotateLeft(camera);
            Collision.unconverX(size.getSize() / 2, position);
        } else if (x > App.getMap().getWidth() - size.getSize() / 2) {
            rotateRight(camera);
            Collision.unconverX(App.getMap().getWidth() - size.getSize() / 2, position);
        }

        camera.getCamera().position.x = 5 + (float) (Math.sin(camera.angle) * camera.getHeight());
        camera.getCamera().position.y = position.getY();
        camera.getCamera().position.z = -5 + (float) (Math.cos(camera.angle) * camera.getHeight());
        camera.getCamera().lookAt(5, position.getY(), -5);

        camera = App.getAvatarCamera();
        camera.getCamera().position.x = 5 + (float) (Math.sin(camera.angle) * camera.getHeight());
        camera.getCamera().position.y = position.getY();
        camera.getCamera().position.z = -5 + (float) (Math.cos(camera.angle) * camera.getHeight());
        camera.getCamera().lookAt(5, position.getY(), -5);

    }

    private void rotateRight(FollowCameraComponent camera) {
        System.out.println("right");
        camera.angle += Math.PI / 2f;
        App.setCurrentView((App.getCurrentView() + 1) % 4);
    }

    private void rotateLeft(FollowCameraComponent camera) {
        System.out.println("left " + (App.getCurrentView() + 3) % 4);
        camera.angle -= Math.PI / 2f;
        App.setCurrentView((App.getCurrentView() + 3) % 4);
    }
}
