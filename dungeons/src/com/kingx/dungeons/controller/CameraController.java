package com.kingx.dungeons.controller;

import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.EntityListener;

public abstract class CameraController {

    protected Camera camera;
    private EntityListener listener;
    private Entity controller;

    public CameraController(Camera camera) {
        this.camera = camera;
        this.listener = createListener();
    }

    public void setController(Entity newController) {
        if(this.controller != null){
        this.controller.unregisterListener(listener);
        }
        this.controller = newController;
        newController.registerListener(listener);
        init(newController);
    }

    public Camera getCamera() {
        return camera;
    }

    protected abstract EntityListener createListener();
    protected abstract void init(Entity puppet);
}
