package com.kingx.dungeons.entity;

import com.badlogic.gdx.graphics.Camera;
import com.kingx.dungeons.App;

public abstract class RenderableEntity extends Entity {
    private boolean visible = true;
    private boolean init = true;

    public RenderableEntity(float x, float y, float z, float size, float speed) {
        super(x, y, z, size, speed);
    }

    public void render() {
        render(App.getDefaultCam());
    }

    public void render(Camera cam) {
        if (visible) {
            if (init) {
                initRender();
                init = false;
            }
            doRender(cam);
        }
    }

    /**
     * Method overidable by subclasses, do not call directly
     */
    protected abstract void initRender();

    /**
     * Method overidable by subclasses, do not call directly
     * 
     * @param cam
     *            camera from which perspective will be rendered
     */
    protected abstract void doRender(Camera cam);

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
