package com.kingx.dungeons.engine.system.server;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.server.ServerCommand;

public final class Decoder extends EntitySystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<MoveComponent> moveMapper;
    @Mapper
    ComponentMapper<RotationComponent> rotationMapper;
    @Mapper
    ComponentMapper<SizeComponent> sizeMapper;

    private final World world;

    private final ArrayList<ServerCommand> buffer = new ArrayList<ServerCommand>();

    public Decoder(World world) {
        super(Aspect.getAspectForAll(PositionComponent.class, MoveComponent.class, RotationComponent.class, SizeComponent.class));
        this.world = world;
    }

    public void recieve(ServerCommand c) {
        buffer.add(c);
    }

    // FIXME operations with this array should be atomic. List can't be accessed during loop
    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        for (ServerCommand c : buffer) {
            Entity e = world.getEntity(c.getId());
            if (e == null) {
                e = world.createEntity(c.getId());
            }

            switch (c.getComponent()) {

                case 0:
                    postionMapper.get(e).setComponent(c.getComponentProperty(), c.getValue());
                    break;
                case 1:
                    moveMapper.get(e).setComponent(c.getComponentProperty(), c.getValue());
                    break;
                case 2:
                    rotationMapper.get(e).setComponent(c.getComponentProperty(), c.getValue());
                    break;
                case 3:
                    sizeMapper.get(e).setComponent(c.getComponentProperty(), c.getValue());
                    break;

            }

        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

}
