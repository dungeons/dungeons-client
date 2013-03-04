package com.kingx.dungeons.graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Helper class with useful color constants for shaders
 */
public enum Colors {
    SHADOW(0.25f, 0.2f, 0.13f, 1f),
    BASE(0.25f, 0.2f, 0.13f, 1f),
    GROUND(0.40f, 0.36f, 0.21f, 1f),
    WALL_LIGHT(0.50f, 0.44f, 0.32f, 1f),
    WALL_SHADOW(0.27f, 0.21f, 0.14f, 1f),
    AVATAR(1, 1, 1, 1f);

    public final Color color;

    private Colors(float r, float g, float b, float a) {
        this.color = new Color(r, g, b, a);
    }

}
