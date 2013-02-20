package com.kingx.dungeons.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.kingx.dungeons.controller.PositionCamera;

public abstract class ShadowCastingEntity extends CleverEntity {

    private final Camera[] lights;

    public ShadowCastingEntity(float x, float y, float z, float size, float speed) {
        super(x, y, z, size, speed);

        lights = new Camera[4];
        int offset = 1;
        for (int i = 0; i < lights.length; i++) {
            lights[i] = new PerspectiveCamera(90, 512, 512);
            lights[i].near = 0.0001f;
            lights[i].far = 500;
            lights[i].direction.x = Math.round(Math.cos(Math.PI / 2 * (i + offset)));
            lights[i].direction.y = Math.round(Math.sin(Math.PI / 2 * (i + offset)));
            lights[i].direction.z = 0.01f;
            lights[i].position.z = 0.1f;
            PositionCamera controller = new PositionCamera(lights[i]);
            controller.setController(this);
        }
    }

    public Camera[] getLights() {
        return lights;
    }
}
