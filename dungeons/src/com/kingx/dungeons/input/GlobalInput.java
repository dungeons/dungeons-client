package com.kingx.dungeons.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.kingx.dungeons.App;

public class GlobalInput extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.Q:
                App.toggleFps();
                break;
        }
        return false;
    }
}
