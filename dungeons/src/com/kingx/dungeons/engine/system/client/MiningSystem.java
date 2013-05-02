package com.kingx.dungeons.engine.system.client;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.AnimationComponent;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.MiningComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.geom.Point.Int;
import com.kingx.dungeons.graphics.cube.Cube;
import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;
import com.kingx.dungeons.tween.CubeAccessor;

public class MiningSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<MiningComponent> miningMapper;
    @Mapper
    ComponentMapper<CollisionComponent> collisionMapper;
    @Mapper
    ComponentMapper<PositionComponent> positionMapper;
    @Mapper
    ComponentMapper<AnimationComponent> animationMapper;

    public MiningSystem() {
        super(Aspect.getAspectForAll(MiningComponent.class, CollisionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        final MiningComponent mining = miningMapper.get(e);
        final CollisionComponent collision = collisionMapper.get(e);
        final PositionComponent position = positionMapper.get(e);

        if (mining.isMining()) {
            if (position.isAnimation()) {
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
                Cube c = App.getCubeManager().getCube(fblock);
                if (c.getType().getHardness() == 0) {
                    return;
                }

                position.setAnimation(true);

                if (animationMapper.has(e)) {
                    animationMapper.get(e).play("dig");
                }

                c.setVisibleSide(c.getRegion(), CubeSideType.BACK, true);

                c.setVisible(false);

                float pick = 10;
                pick = 30;
                pick = 90;
                float time = Math.max(c.getType().getHardness() / pick, 0.2f);
                App.getCubeManager().checkCubeRegion(App.getCurrentView(), c.getX(), c.getY());
                Tween.to(c, CubeAccessor.SCALE, time).target(0).start(App.getTweenManager());
                Animation.translateAtoB(position.get(), fblock, time, new TweenCallback() {

                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        App.getCubeManager().removeCube(fblock);
                        mining.setMining(false);
                        position.setAnimation(false);
                    }
                });
            }

        }
    }
}
