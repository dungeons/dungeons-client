package com.kingx.dungeons.input;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.kingx.dungeons.engine.component.MoveComponent;

public final class InputManager {

    private final static InputManager instance = new InputManager();
    private final InputMultiplexer im = new InputMultiplexer();
    private final HashMap<InputSet, PlayerInput> map = new HashMap<InputSet, PlayerInput>();

    private InputManager() {
        Gdx.input.setInputProcessor(im);
        im.addProcessor(new GlobalInput());
    };

    public static InputManager getInstance() {
        return instance;
    }

    public boolean registerInput(InputSet is, MoveComponent ic) {
        PlayerInput e = map.get(is);
        if (e == null) {
            PlayerInput pi = new PlayerInput(is, ic);
            im.addProcessor(pi);
            map.put(is, pi);
            return true;
        }
        return false;
    }
}
