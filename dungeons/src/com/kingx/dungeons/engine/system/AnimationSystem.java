package com.kingx.dungeons.engine.system;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.engine.component.AnimationComponent;

public class AnimationSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<AnimationComponent> animationMapper;
    private int cycle;

    public AnimationSystem() {
        super(Aspect.getAspectForAll(AnimationComponent.class));
    }

    @Override
    protected void process(Entity e) {
        AnimationComponent component = animationMapper.get(e);
        component.next();
    }

    @Override
    protected boolean checkProcessing() {
        if (cycle == 10) {
            cycle = 0;
            return true;
        } else {
            cycle++;
            return false;
        }
    }

}
