package com.kingx.dungeons.controller;

import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.entity.EntityListener;

public class PositionCamera extends CameraController {

    public PositionCamera(Camera camera) {
        super(camera);
    }

    @Override
    protected EntityListener createListener() {
        return new EntityListener() {

            @Override
            public void positionChange(float x, float y, float z) {
                camera.position.x = x;
                camera.position.y = y;
            }

            @Override
            public void rotationChange(float rotation) {
            }
        };

    }

}
