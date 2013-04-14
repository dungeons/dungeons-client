package com.kingx.dungeons.graphics;

import com.badlogic.gdx.graphics.Color;
import com.kingx.dungeons.App;

/**
 * Helper class with useful color constants for shaders
 */
public class Colors {
    public static final Color SHADOW = new Color(0.15f, 0.11f, 0.06f, 1f);
    public static final Color BASE = new Color(0.25f, 0.2f, 0.13f, 1f);
    public static final Color GROUND = new Color(0.40f, 0.36f, 0.21f, 1f);
    public static final Color WALL_LIGHT = new Color(1f, 1f, 1f, 1f);
    public static final Color WALL_SHADOW = new Color(0.4f, 0.4f, 0.4f, 1f);
    public static final Color ZOMBIE_NORMAL = new Color(0.21f, 0.15f, .1f, 1f);
    public static final Color ZOMBIE_ALARM = new Color(0.7f, 0.2f, 0.2f, 1f);
    public static final Color AVATAR = new Color(1, 1, 1, 1f);

    public static final Color DEBUG_ZOMBIE_IDLE = new Color(1, 1, 1, 1f);
    public static final Color DEBUG_ZOMBIE_SEARCH = new Color(0, 0, 1f, 1f);
    public static final Color DEBUG_ZOMBIE_SEE = new Color(0, 1, 0, 1f);
    public static final Color DEBUG_ZOMBIE_ATTACK = new Color(1, 0, 0, 1f);
    public static final Color SKY = Color.BLACK;
    public static final Color SHADOW_BOTTOM = new Color(0.5f, 0.7f, 0.16f, 1f);
    public static final Color WORLD_BOTTOM = new Color(0.085f, 0.117f, 0.051f, 1f);

    public static Color random() {
        float r = App.rand.nextFloat();
        float g = App.rand.nextFloat();
        float b = App.rand.nextFloat();
        float a = App.rand.nextFloat();
        return new Color(r, g, b, a);
    }

    public static Color interpolate(Color a, Color b, float stage, float gradient) {
        Color result = new Color();
        result.r = interpolateChannel(a.r, b.r, stage, gradient);
        result.g = interpolateChannel(a.g, b.g, stage, gradient);
        result.b = interpolateChannel(a.b, b.b, stage, gradient);
        result.a = interpolateChannel(a.a, b.a, stage, gradient);
        return result;
    }

    public static float interpolateChannel(float a, float b, float stage, float gradient) {
        return a + ((b - a) * stage / gradient);
    }
}
