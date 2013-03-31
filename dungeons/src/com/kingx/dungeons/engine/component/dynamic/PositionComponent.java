package com.kingx.dungeons.engine.component.dynamic;

import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.graphics.cube.CubeRegion;

public class PositionComponent extends AbstractComponent {
    public Vector3 inWorld;

    public PositionComponent(float x, float y, float z) {
        this.inWorld = new Vector3(x, y, z);
    }

    public PositionComponent(Vector3 vector) {
        this.inWorld = vector;
    }

    public float getX() {
        return inWorld.x;
    }

    public float getY() {
        return inWorld.y;
    }

    public float getZ() {
        return inWorld.z;
    }

    public void setX(float x) {
        inWorld.x = x;
    }

    public void setY(float y) {
        inWorld.y = y;
    }

    public void setZ(float z) {
        inWorld.z = z;
    }

    @Override
    public void setComponent(int id, int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return inWorld.toString();
    }

    public void set(Vector3 vec) {
        this.inWorld.set(vec);
    }

    public Vector3 get() {
        return inWorld;
    }

    public float getScreenX() {
        switch (App.getCurrentView()) {
            case 0:
                return getX() + CubeRegion.min.x;
            case 1:
                return -getZ() + CubeRegion.max.z;
            case 2:
                return CubeRegion.max.x - getX();
            case 3:
                return getZ() - CubeRegion.min.z;
        }
        return 0;
    }

    public void setScreenX(float x) {
        switch (App.getCurrentView()) {
            case 0:
                setX(x + CubeRegion.min.x);
                break;
            case 1:
                setZ(-x + CubeRegion.max.z);
                break;
            case 2:
                setX(-x + CubeRegion.max.x);
                break;
            case 3:
                setZ(x + CubeRegion.min.z);
                break;
        }
    }
}
