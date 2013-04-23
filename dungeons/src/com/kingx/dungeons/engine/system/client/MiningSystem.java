package com.kingx.dungeons.engine.system.client;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.MiningComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.geom.Point.Int;

public class MiningSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<MiningComponent> miningMapper;
    @Mapper
    ComponentMapper<CollisionComponent> collisionMapper;
    @Mapper
    ComponentMapper<PositionComponent> positionMapper;

    public MiningSystem() {
        super(Aspect.getAspectForAll(MiningComponent.class, CollisionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        final MiningComponent mining = miningMapper.get(e);
        final CollisionComponent collision = collisionMapper.get(e);
        PositionComponent position = positionMapper.get(e);

        if (mining.isMining()) {
            if (mining.isAnimation()) {
                return;
            }
            Int block = null;
            if (collision.getLeft() != null) {
                block = collision.getLeft();
            } else if (collision.getRight() != null) {
                block = collision.getRight();
            }

            if (block == null && collision.getDown() != null) {
                block = collision.getDown();
            }

            if (block != null) {
                final Int fblock = block;
                mining.setAnimation(true);
                Animation.translateAtoB(position.get(), fblock, 0.5f, new TweenCallback() {

                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        App.getCubeManager().removeCube(fblock);
                        mining.setMining(false);
                        mining.setAnimation(false);
                    }
                });
            }

        }
    }
}
