package com.kingx.dungeons.input;

import com.badlogic.gdx.InputAdapter;
import com.kingx.dungeons.App;
import com.kingx.dungeons.server.ClientCommand;

public class Input extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
        return action(keycode, 1);
    }

    @Override
    public boolean keyUp(int keycode) {
        return action(keycode, 0);
    }

    private boolean action(int keycode, int dir) {
        send(new ClientCommand((short) keycode, System.currentTimeMillis(), dir));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        send(new ClientCommand((short) InputConstants.TOUCH_X_DOWN, System.currentTimeMillis(), screenX));
        send(new ClientCommand((short) InputConstants.TOUCH_Y_DOWN, System.currentTimeMillis(), screenY));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        send(new ClientCommand((short) InputConstants.TOUCH_X_UP, System.currentTimeMillis(), screenX));
        send(new ClientCommand((short) InputConstants.TOUCH_Y_UP, System.currentTimeMillis(), screenY));
        return false;
    }

    private void send(ClientCommand cc) {

        if (App.INITIALIZED) {
            App.getServer().send(cc);
        }
    }
}
