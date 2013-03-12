package com.kingx.dungeons.server;

import com.kingx.dungeons.Logic;

public class OfflineServer extends AbstractServer {

    public OfflineServer(Logic logic) {
        super(logic);
    }

    @Override
    public void send(Command c) {
        logic.executeClientCommand(c);
    }

    @Override
    public Command recieve() {
        return null;
    }

}
