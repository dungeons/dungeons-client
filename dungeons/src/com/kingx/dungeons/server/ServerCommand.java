package com.kingx.dungeons.server;

public class ServerCommand {

    private final short id;
    private final short action;
    private final int value;

    public ServerCommand(short id, short action, int value) {
        this.id = id;
        this.action = action;
        this.value = value;
    }

    public short getId() {
        return id;
    }

    public short getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }

}
