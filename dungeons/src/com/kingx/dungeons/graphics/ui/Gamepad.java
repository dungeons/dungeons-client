package com.kingx.dungeons.graphics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kingx.dungeons.graphics.UI;

public class Gamepad implements UI {

    private final NinePath leftPad;
    private final NinePath rightPad;
    private final ShapeRenderer renderer;

    public Gamepad(ShapeRenderer renderer) {
        this.renderer = renderer;
        int quarter = Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 4;
        int offset = 20;

        leftPad = new NinePath(offset, offset, quarter, quarter, renderer, new int[] { Keys.W, Keys.S, Keys.A, Keys.D });
        rightPad = new NinePath(Gdx.graphics.getWidth() - quarter - offset, offset, quarter, quarter, renderer, new int[] { Keys.SPACE, -1, Keys.E, -1 });

    }

    @Override
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1, 1, 1, 0.4f);

        leftPad.drawUp(NinePathType.BOX);
        leftPad.drawDown(NinePathType.BOX);
        leftPad.drawLeft(NinePathType.BOX);
        leftPad.drawRight(NinePathType.BOX);

        rightPad.drawUp(NinePathType.CIRCLE);
        rightPad.drawDown(NinePathType.CIRCLE);
        rightPad.drawLeft(NinePathType.CIRCLE);
        rightPad.drawRight(NinePathType.CIRCLE);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void hitKey(int x, int y, int index) {
        NinePath.hit(x, y, index);
    }

    public void releaseKey(int index) {
        NinePath.release(index);
    }
}
