package com.kingx.dungeons.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.entity.graphics.Shader;

public abstract class RenderableEntity extends Entity {
    private boolean visible = true;

    public RenderableEntity(float x, float y, float z, float size, float speed) {
        super(x, y, z, size, speed);
        initRender();
    }

    public void render() {
        if (visible) {
            doRender(App.getDefaultCam());
        }
    }

    public void render(Camera cam) {
        if (visible) {
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
     * @param cam camera from which perspective will be rendered
     */
    protected abstract void doRender(Camera cam);

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Vector3 toOrigin() {
        return new Vector3(this.getPositionX() + this.size / 2, this.getPositionY() + this.size / 2, this.getPositionZ());
    }

}
