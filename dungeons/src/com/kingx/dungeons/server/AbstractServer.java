package com.kingx.dungeons.server;

import com.kingx.dungeons.Logic;

public abstract class AbstractServer implements Server {
    protected Logic logic;

    public AbstractServer(Logic logic) {
        this.logic = logic;
    }

    @Override
    public void send(Command c) {
        logic.executeClientCommand(c);
    }

}
