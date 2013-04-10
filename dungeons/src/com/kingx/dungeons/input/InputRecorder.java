package com.kingx.dungeons.input;

import com.kingx.dungeons.GameStateManager;
import com.kingx.dungeons.server.ClientCommand;

public class InputRecorder extends RegularInputProcessor {

    private final GameStateManager state;

    public InputRecorder(GameStateManager state) {
        this.state = state;
    }

    @Override
    public void process(ClientCommand c) {
        super.process(c);
        state.register(c);
    }

}
