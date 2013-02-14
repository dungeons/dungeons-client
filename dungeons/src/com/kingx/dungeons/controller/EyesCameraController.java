package com.kingx.dungeons.controller;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.entity.EntityListener;

public class EyesCameraController extends CameraController {

    private float rotation;

    public EyesCameraController(Camera camera) {
        super(camera);
    }

    @Override
    protected EntityListener createListener() {

        return new EntityListener() {
            private Vector3 axis = new Vector3(0, 0, 1);

            @Override
            public void positionChange(float x, float y, float z) {
                camera.position.x = x;
                camera.position.y = y + 10;
                camera.position.z = 3;
                camera.lookAt(x, y - 10, 0);
            }

            @Override
            public void rotationChange(float rotation) {
                EyesCameraController.this.rotation = rotation;
                adjustCamera();
            }
        };

    }

    protected void adjustCamera() {
        // TODO Auto-generated method stub

    }

}
