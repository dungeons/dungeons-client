package com.kingx.dungeons.server;

import java.util.ArrayList;

import com.kingx.artemis.World;
import com.kingx.dungeons.Updateable;

public abstract class AbstractServer implements Updateable {
    protected World world;
    private final ArrayList<ClientCommand> buffer = new ArrayList<ClientCommand>();

    public AbstractServer(World world) {
        this.world = world;
    }

    public void send(ClientCommand c) {
        buffer.add(c);
    }

    @Override
    public void update(float delta) {
        for (ClientCommand c : buffer) {
            process(c);
        }
        buffer.clear();
        updateInternal(delta);
    }

    public abstract void process(ClientCommand c);

    public abstract void updateInternal(float delta);

}
