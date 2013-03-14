package com.kingx.dungeons.server;

import com.artemis.Entity;
import com.artemis.World;

public class Client {

    private final World world;

    public Client(World world) {
        this.world = world;
    }

    public void recieve(ServerCommand c) {
        Entity e = world.getEntity(c.getId());
        if (e == null) {
            e = createEntity(c.getId());
        }
    }

    private Entity createEntity(short id) {
        // TODO create entity and assing it to an entity array.
        return null;
    }

}
