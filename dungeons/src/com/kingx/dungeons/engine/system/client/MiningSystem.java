package com.kingx.dungeons.engine.system.client;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.MiningComponent;

public class MiningSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<MiningComponent> miningMapper;
    @Mapper
    ComponentMapper<CollisionComponent> collisionMapper;

    public MiningSystem() {
        super(Aspect.getAspectForAll(MiningComponent.class, CollisionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        MiningComponent mining = miningMapper.get(e);
        CollisionComponent collision = collisionMapper.get(e);

        if (mining.isMining()) {
            if (collision.getLeft() != null) {
                App.getCubeManager().removeCube(collision.getLeft());
            }
        }
    }

}
