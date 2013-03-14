package com.kingx.dungeons.server;

public class ClientCommand {

    private final short action;
    private final long timestamp;
    private final int value;

    public ClientCommand(short action, long timestamp, int value) {
        this.action = action;
        this.timestamp = timestamp;
        this.value = value;
    }

    public short getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getValue() {
        return value;
    }

}
