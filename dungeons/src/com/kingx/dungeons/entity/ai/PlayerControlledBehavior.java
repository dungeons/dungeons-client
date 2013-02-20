package com.kingx.dungeons.entity.ai;

import com.badlogic.gdx.InputProcessor;
import com.kingx.dungeons.entity.Entity;

public abstract class PlayerControlledBehavior extends AbstractBehavior implements InputProcessor {

    public PlayerControlledBehavior() {
        super(null);
    }

    public PlayerControlledBehavior(Entity puppet) {
        super(puppet);
    }

}
