package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Camera;

public class FollowCameraComponent extends Component {
    public Camera camera;
    public float height;

    public FollowCameraComponent(Camera camera, float height) {
        this.camera = camera;
        this.height = height;
    }

}
