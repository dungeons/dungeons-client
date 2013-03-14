package com.kingx.dungeons.server;

import java.util.ArrayList;

import com.artemis.World;

public class OfflineServer extends AbstractServer {

    private final World world;

    public OfflineServer(Client client) {
        super(client);
        world = new World();
    }

    @Override
    public void update(float delta) {
        world.setDelta(delta);
        world.process();

        this.recieve(null);
    }

    @Override
    public void process(ArrayList<Command> buffer) {
        // TODO Auto-generated method stub
    }

}
