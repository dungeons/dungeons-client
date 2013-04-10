package com.kingx.dungeons.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.kingx.dungeons.App;
import com.kingx.dungeons.server.ClientCommand;

public class Input extends InputAdapter {

    private final InputPostProcessor processor;

    public Input(InputPostProcessor processor) {
        this.processor = processor;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("Key down: " + keycode);
        return action(keycode, 1);
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("Key up: " + keycode);
        return action(keycode, 0);
    }

    private boolean action(int keycode, int dir) {
        System.out.println(App.INITIALIZED);
        if (App.INITIALIZED) {
            processor.process(new ClientCommand((short) keycode, System.currentTimeMillis(), dir));
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (App.getGamepad() != null) {
            App.getGamepad().hitKey(screenX, invertY(screenY), pointer);
        }
        return false;
    }

    private int invertY(int y) {
        return Gdx.graphics.getHeight() - y;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (App.getGamepad() != null) {
            App.getGamepad().releaseKey(pointer);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (App.getGamepad() != null) {
            App.getGamepad().hitKey(screenX, invertY(screenY), pointer);
        }
        return false;
    }

    private void rotateCamera(float v) {
        App.getWorldCamera().arbitratyAngle += v;
    }
}
