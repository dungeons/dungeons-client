package com.kingx.dungeons.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.kingx.dungeons.graphics.cube.Cube;

public class CubeAccessor implements TweenAccessor<Cube> {

    public static final int SCALE = 1;

    @Override
    public int getValues(Cube target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case SCALE:
                returnValues[0] = target.scale;
        }
        return 1;
    }

    @Override
    public void setValues(Cube target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case SCALE:
                target.scale = newValues[0];
        }
    }
}
