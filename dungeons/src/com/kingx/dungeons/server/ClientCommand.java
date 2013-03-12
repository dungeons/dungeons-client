package com.kingx.dungeons.server;

public class ClientCommand extends Command {

    private static final int ACTION = 2;
    private static final int TIMESTAMP = 8;
    private static final int VALUE = 4;

    public ClientCommand() {
        super(ACTION + TIMESTAMP + VALUE);
    }

    public ClientCommand(short action, long timestamp, int value) {
        super(ACTION + TIMESTAMP + VALUE);
        this.buffer.putShort(action);
        this.buffer.putLong(timestamp);
        this.buffer.putInt(value);
    }

}
