package com.kingx.dungeons.server;

import java.util.ArrayList;

import com.kingx.dungeons.engine.system.Decoder;

public abstract class AbstractServer implements Server {
    protected Decoder client;
    private final ArrayList<ClientCommand> buffer = new ArrayList<ClientCommand>();

    public AbstractServer(Decoder client) {
        this.client = client;
    }

    @Override
    public void send(ClientCommand c) {
        buffer.add(c);
    }

    @Override
    public void recieve(ServerCommand c) {
        client.recieve(c);
    }

    @Override
    public void update(float step) {
        for (ClientCommand c : buffer) {
            process(c);
        }
        buffer.clear();
    }

    public abstract void process(ClientCommand c);

}
