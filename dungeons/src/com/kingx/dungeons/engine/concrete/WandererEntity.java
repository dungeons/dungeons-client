package com.kingx.dungeons.engine.concrete;

import com.artemis.Entity;
import com.artemis.World;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.SizeComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;


public class WandererEntity extends ConcreteEntity {

    private final World world;
    private final float x;
    private final float y;
    private final float size;
    private final float speed;

    public WandererEntity(World world, float x, float y, float size, float speed) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    @Override
    public Entity createEntity() {
        Entity e = world.createEntity();

        PositionComponent positionComponent = new PositionComponent(x, y, size);
        SizeComponent sizeComponent = new SizeComponent(size);
        SpeedComponent speedComponent = new SpeedComponent(speed);

        e.addComponent(positionComponent);
        e.addComponent(sizeComponent);
        e.addComponent(speedComponent);

        return e;
    }

}
