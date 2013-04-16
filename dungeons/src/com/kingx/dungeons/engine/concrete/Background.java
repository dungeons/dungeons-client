package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.component.BackgroundComponent;

public class Background extends ConcreteEntity {

    private final BackgroundComponent component;

    public Background(World world, Vector3 position, String name) {
        super(world);
        component = new BackgroundComponent(position, name);
        bag.add(component);
    }

    public BackgroundComponent getComponent() {
        return component;
    }
}
