package com.kingx.dungeons.entity.ai;

import com.kingx.dungeons.entity.Entity;

public interface Behavior {

    public void move(float delta);

    public void setPuppet(Entity puppet);

    public void update(float delta);

}
