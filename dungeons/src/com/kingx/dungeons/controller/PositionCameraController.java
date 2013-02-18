package com.kingx.dungeons.controller;

import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.EntityListener;

public class PositionCameraController extends CameraController {

    public PositionCameraController(Camera camera) {
        super(camera);
    }

    @Override
    protected EntityListener createListener() {
        return new EntityListener() {

            @Override
            public void positionChange(float x, float y, float z) {
                camera.position.x = x;
                camera.position.y = y;
                camera.lookAt(x, y, z);
            }

            @Override
            public void rotationChange(float rotation) {
            }
        };

    }

    @Override
    protected void init(Entity puppet) {
    }

}
