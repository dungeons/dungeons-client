package com.kingx.dungeons.input;

import com.kingx.dungeons.GameStateManager;
import com.kingx.dungeons.Updateable;

public class InputPlayer implements Updateable {

    private final GameStateManager state;

    public InputPlayer(GameStateManager state, Input input) {
        this.state = state;
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub

    }

}
