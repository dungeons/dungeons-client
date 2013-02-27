package com.kingx.dungeons.input;

import com.badlogic.gdx.InputProcessor;
import com.kingx.dungeons.engine.component.InputComponent;

public final class PlayerInput implements InputProcessor {

    private final InputSet keys;
    private final InputComponent components;

    public PlayerInput(InputSet is, InputComponent ic) {
        keys = is;
        components = ic;
    }

    /// input

    @Override
    public boolean keyDown(int keycode) {
        return action(keycode, 1);
    }

    @Override
    public boolean keyUp(int keycode) {
        return action(keycode, -1);
    }

    private boolean action(int keycode, int dir) {
        if (keycode == keys.getUp()) {
            components.vector.add(0, 1 * dir);
        } else if (keycode == keys.getDown()) {
            components.vector.add(0, -1 * dir);
        } else if (keycode == keys.getLeft()) {
            components.vector.add(-1 * dir, 0);
        } else if (keycode == keys.getRight()) {
            components.vector.add(1 * dir, 0);
        }
        return false;
    }

    // TODO Create input for touch devices
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    // NOTE currently not planned to future use
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
