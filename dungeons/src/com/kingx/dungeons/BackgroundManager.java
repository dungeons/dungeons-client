package com.kingx.dungeons;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.concrete.Background;
import com.kingx.dungeons.tween.BackgroundAccessor;

public class BackgroundManager {

    private final World world;
    private final int width;
    private final int height;
    private final List<Background> backgrounds = new ArrayList<Background>();

    public BackgroundManager(World world, int width, int height) {
        this.world = world;
        this.width = width;
        this.height = height;
    }

    public void addBackground(float x, float y, String name) {
        Vector3 p = new Vector3(x, height, y);
        Background b = new Background(world, p, name);
        b.createEntity().addToWorld();
        backgrounds.add(b);
    }

    public void setFace(int currentView) {
        for (Background background : backgrounds) {

        }

    }

    public void hide(int view) {
        for (Background background : backgrounds) {
            Tween.to(background.getComponent().getTexture(view), BackgroundAccessor.FADE, 1.0f).target(0).ease(Quad.OUT).start(App.getTweenManager());
        }
    }

    public void show(int view) {
        for (Background background : backgrounds) {
            Tween.to(background.getComponent().getTexture(view), BackgroundAccessor.FADE, 1.0f).target(1f).ease(Quad.OUT).start(App.getTweenManager());
        }
    }

}
