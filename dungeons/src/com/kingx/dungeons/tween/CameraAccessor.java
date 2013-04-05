package com.kingx.dungeons.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.kingx.dungeons.engine.component.FollowCameraComponent;

public class CameraAccessor implements TweenAccessor<FollowCameraComponent> {

    public static final int ROTATION_Y = 1;

    @Override
    public int getValues(FollowCameraComponent target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ROTATION_Y:
                returnValues[0] = target.angle;
        }
        return 1;
    }

    @Override
    public void setValues(FollowCameraComponent target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ROTATION_Y:
                target.angle = newValues[0];
        }
    }
}
