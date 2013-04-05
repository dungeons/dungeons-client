package com.kingx.dungeons.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.kingx.dungeons.App;
import com.kingx.dungeons.server.ClientCommand;

public class Input extends InputAdapter {

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
        if (App.INITIALIZED) {
            switch (keycode) {
                case Keys.PLUS:
                    rotateCamera(0.1f);
                    break;
                case Keys.MINUS:
                    rotateCamera(-0.1f);
                    break;
                default:
                    send(new ClientCommand((short) keycode, System.currentTimeMillis(), dir));
            }
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

    private void send(ClientCommand cc) {
        App.getServer().send(cc);
    }

    private void rotateCamera(float v) {
        App.getWorldCamera().arbitratyAngle += v;
    }
}
