package com.kingx.dungeons.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.math.Vector3;

public class Vector3Accessor implements TweenAccessor<Vector3> {

    public static final int XYZ = 0;

    @Override
    public int getValues(Vector3 target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case XYZ:
                returnValues[0] = target.x;
                returnValues[1] = target.y;
                returnValues[2] = target.z;
                break;
        }
        return 3;
    }

    @Override
    public void setValues(Vector3 target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case XYZ:
                target.x = newValues[0];
                target.y = newValues[1];
                target.z = newValues[2];
                break;
        }
    }
}