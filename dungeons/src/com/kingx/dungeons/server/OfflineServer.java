package com.kingx.dungeons.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.artemis.World;

public class OfflineServer extends OnlineServer {

    private final World world;

    public OfflineServer(Client client) {
        super(client);
        world = new World();
    }

    @Override
    protected Socket connect() throws UnknownHostException, IOException {
        return new Socket();
    }

    @Override
    public void process(ClientCommand c) {
        //TODO logic
        client.recieve(new ServerCommand((short) 1, (short) 1, 20));
    }

}
