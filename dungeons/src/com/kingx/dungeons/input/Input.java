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

        if (App.INITIALIZED) {
            App.getServer().send(new ClientCommand((short) keycode, System.currentTimeMillis(), dir));
        }
        return false;
    }
}
