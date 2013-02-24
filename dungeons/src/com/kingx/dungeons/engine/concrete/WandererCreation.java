package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.kingx.dungeons.engine.component.InputComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;

public class WandererCreation extends ConcreteEntity {

    private final World world;
    private final float x;
    private final float y;
    private final float size;
    private final float speed;

    public WandererCreation(World world, float x, float y, float size, float speed) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    @Override
    public Entity createEntity() {
        Entity e = world.createEntity();

        e.addComponent(new PositionComponent(x, y, size / 2f));
        e.addComponent(new SpeedComponent(speed));
        e.addComponent(new InputComponent(0, 0));
        e.addComponent(new SizeComponent(size));
        return e;
    }
}
