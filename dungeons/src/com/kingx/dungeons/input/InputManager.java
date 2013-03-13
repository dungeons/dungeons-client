package com.kingx.dungeons.input;

import com.badlogic.gdx.Gdx;

public final class InputManager {

    private final static InputManager instance = new InputManager();

    private InputManager() {
        Gdx.input.setInputProcessor(new Input());
    };

    public static InputManager getInstance() {
        return instance;
    }
}
