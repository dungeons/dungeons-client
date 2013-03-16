package com.kingx.dungeons.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.artemis.World;
import com.kingx.dungeons.engine.system.CollisionSystem;
import com.kingx.dungeons.engine.system.Decoder;
import com.kingx.dungeons.engine.system.MovementSystem;
import com.kingx.dungeons.engine.system.ai.ZombieAI;

public class OfflineServer extends OnlineServer {

    private final World world;

    public OfflineServer(Decoder client) {
        super(client);
        world = new World();
        world.setSystem(new MovementSystem());
        world.setSystem(new ZombieAI());
        world.setSystem(new CollisionSystem());
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
