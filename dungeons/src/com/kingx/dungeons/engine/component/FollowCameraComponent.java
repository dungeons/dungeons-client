package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.Camera;
import com.kingx.artemis.Component;

public class FollowCameraComponent extends Component {
    private final Camera camera;
    private final float height;

    public FollowCameraComponent(Camera camera, float height) {
        this.camera = camera;
        this.height = height;
    }

    public Camera getCamera() {
        return camera;
    }

    public float getHeight() {
        return height;
    }

}
