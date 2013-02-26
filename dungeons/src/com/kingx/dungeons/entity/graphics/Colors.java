package com.kingx.dungeons.entity.graphics;

import com.badlogic.gdx.graphics.Color;

public enum Colors {
    SHADOW(0.4f, 0.4f, 0.4f, 1f), BASE(0.2f, 0.2f, 0.2f, 1f), GROUND(0.3f, 0.3f, 0.3f, 1f), AVATAR(1, 1, 1, 1f);
    public final Color color;

    private Colors(float r, float g, float b, float a) {
        this.color = new Color(r, g, b, a);
    }

}
