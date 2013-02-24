package com.kingx.dungeons.engine.input;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.kingx.dungeons.engine.component.InputComponent;


public final class InputManager {

    private final static InputManager instance = new InputManager();
    private InputMultiplexer im = new InputMultiplexer();
    private HashMap<InputSet, PlayerInput> map = new HashMap<InputSet, PlayerInput>();

    private InputManager() {
        Gdx.input.setInputProcessor(im);
    };

    public static InputManager getInstance() {
        return instance;
    }

    public boolean registerInput(InputSet is, InputComponent ic) {
        PlayerInput e = map.get(is);
        if (e == null) {
            map.put(is, new PlayerInput(is, ic));
            return true;
        }
        return false;
    }
}
