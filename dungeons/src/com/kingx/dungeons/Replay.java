package com.kingx.dungeons;

import java.util.ArrayList;

import com.kingx.dungeons.server.ClientCommand;

public final class Replay {

    private ArrayList<ClientCommand> buffer;

    public Replay() {
        this.buffer = new ArrayList<ClientCommand>();
    }

    public void registerInput(ClientCommand c) {
        buffer.add(c);
    }

    public ArrayList<ClientCommand> getBuffer() {
        return buffer;
    }

}
