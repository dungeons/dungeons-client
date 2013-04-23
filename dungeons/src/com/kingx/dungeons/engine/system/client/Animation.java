package com.kingx.dungeons.engine.system.client;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.geom.Point.Int;
import com.kingx.dungeons.tween.Vector3Accessor;

public class Animation {

    public static void translateAtoB(Vector3 a, Int b, float duration, TweenCallback callback) {
        Vector3 goal = Collision.screenToWorld(new Vector2(b.x + App.UNIT / 2f, b.y + App.UNIT / 2f));
        translateAtoB(a, goal, duration, callback);
    }

    private static void translateAtoB(Vector3 a, Vector3 b, float duration, TweenCallback callback) {
        Tween.to(a, Vector3Accessor.XYZ, duration).target(b.x, b.y, b.z).setCallback(callback).start(App.getTweenManager());
    }
}
