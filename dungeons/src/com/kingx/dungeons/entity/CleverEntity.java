package com.kingx.dungeons.entity;

import com.kingx.dungeons.entity.ai.Behavior;

public abstract class CleverEntity extends RenderableEntity {

    private boolean active = false;
    private Behavior currentBehavior;

    public CleverEntity(float x, float y, float z, float size, float speed) {
        super(x, y, z, size, speed);
        // TODO Auto-generated constructor stub
    }

    public void update(float delta) {
        if (active) {
            doUpdate(delta);
        }
    }

    protected void doUpdate(float delta) {
        if (currentBehavior != null) {
            currentBehavior.update(delta);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBehavior(Behavior behavior) {
        currentBehavior = behavior;
    }
}
