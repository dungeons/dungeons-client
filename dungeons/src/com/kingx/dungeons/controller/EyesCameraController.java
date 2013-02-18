package com.kingx.dungeons.controller;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.EntityListener;

public class EyesCameraController extends CameraController {

    private Vector3 axis = new Vector3(0, 0, 1);
    private float lastRot;

    private float referenceDistance = 15;
    private float referenceX;
    private float referenceY;
    private float referenceRotation;

    public EyesCameraController(Camera camera) {
        super(camera);
    }

    @Override
    protected EntityListener createListener() {

        return new EntityListener() {

            @Override
            public void positionChange(float x, float y, float z) {
                referenceX = x;
                referenceY = y;
            }

            @Override
            public void rotationChange(float rotation) {
                referenceRotation = rotation;
                adjustCamera();
            }
        };

    }

    @Override
    protected void init(Entity puppet) {
        camera.position.z = 0.1f;
        camera.direction.set(0, 1,0.001f);
        referenceRotation = puppet.getRotation();
        adjustCamera();

    }

    protected void adjustCamera() {
       //w float temp = referenceRotation - lastRot;
        camera.position.x = referenceX  ;
        camera.position.y = referenceY ;
    /*    if (temp > 0) {
            camera.rotate(axis, temp);
            lastRot = referenceRotation;
        }*/
    }

}
