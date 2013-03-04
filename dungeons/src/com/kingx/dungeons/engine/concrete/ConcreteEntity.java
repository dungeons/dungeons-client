package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;

public abstract class ConcreteEntity {
    private final World world;
    private Entity entity;

    public ConcreteEntity(World world) {
        this.world = world;
    }

    public Entity getEntity() {
        if (entity == null) {
            entity = world.createEntity();
        }
        return entity;
    }

    public abstract Entity createEntity();

}
