package com.kingx.dungeons.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.kingx.dungeons.engine.component.TextureComponent;

public class BackgroundAccessor implements TweenAccessor<TextureComponent> {

    public static final int FADE = 1;

    @Override
    public int getValues(TextureComponent target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case FADE:
                returnValues[0] = target.getTint().a;
        }
        return 1;
    }

    @Override
    public void setValues(TextureComponent target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case FADE:
                target.getTint().a = newValues[0];
        }
    }
}