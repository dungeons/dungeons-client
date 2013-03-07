package com.kingx.dungeons.input;

import com.badlogic.gdx.InputAdapter;
import com.kingx.dungeons.engine.component.MoveComponent;

public final class PlayerInput extends InputAdapter {

    private final InputSet keys;
    private final MoveComponent components;

    public PlayerInput(InputSet is, MoveComponent ic) {
        keys = is;
        components = ic;
    }

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
}
