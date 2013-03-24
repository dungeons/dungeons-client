package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.Camera;
import com.kingx.artemis.Component;

public class FollowCameraComponent extends Component {
    public final Camera camera;
    public float height;
    public float angle = (float) (Math.PI / 2f);

    public FollowCameraComponent(Camera camera, float height, float angle) {
        this.camera = camera;
        this.height = height;
        this.angle = angle;
    }

    public Camera getCamera() {
        return camera;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return angle;
    }

}
