package com.kingx.dungeons.server;

import com.kingx.dungeons.App;

public class ClockCommand extends ClientCommand {
    /**
     * 
     */
    private static final long serialVersionUID = 8079538732187185325L;

    public ClockCommand(ClientCommand c) {
        super(c.getAction(), App.getClock().getClocks(), c.getValue());
    }
}
