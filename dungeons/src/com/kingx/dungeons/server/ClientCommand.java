package com.kingx.dungeons.server;

import java.io.Serializable;

public class ClientCommand implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6271064802913973327L;
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

    @Override
    public String toString() {
        return "ClientCommand [action=" + action + ", timestamp=" + timestamp + ", value=" + value + "]";
    }

}
