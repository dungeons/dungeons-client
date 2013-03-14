package com.kingx.dungeons.server;

import java.util.ArrayList;

public abstract class AbstractServer implements Server {
    protected Client client;
    private final ArrayList<Command> buffer = new ArrayList<Command>();

    public AbstractServer(Client client) {
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
        process(buffer);
    }

    public abstract void process(ArrayList<Command> buffer);

}
