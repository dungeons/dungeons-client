package com.kingx.dungeons.server;

public class ServerCommand extends Command {

    private static final int ID = 2;
    private static final int ACTION = 2;
    private static final int VALUE = 4;

    public ServerCommand() {
        super(ID + ACTION + VALUE);
    }

    public ServerCommand(short id, short action, int value) {
        super(ID + ACTION + VALUE);
        this.buffer.putShort(id);
        this.buffer.putShort(action);
        this.buffer.putInt(value);
    }

}
