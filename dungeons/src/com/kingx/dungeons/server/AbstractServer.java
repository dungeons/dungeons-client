package com.kingx.dungeons.server;

import com.kingx.dungeons.Logic;

public abstract class AbstractServer implements Server {

    protected Logic logic;

    public AbstractServer(Logic logic) {
        this.logic = logic;
    }

    public void executeCommand(Command c) {
        logic.executeServerCommand(c);
    }

}
