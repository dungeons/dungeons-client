package com.kingx.dungeons.engine.ai.task;

import com.kingx.artemis.Entity;
import com.kingx.dungeons.engine.ai.controller.TaskController;

public abstract class Task {

    /**
     * Holds current entity for processing.
     */
    protected Entity entity;

    /**
     * Override to do a pre-conditions check to see if the task can be updated.
     * 
     * @return True if it can, false if it can't
     */
    public abstract boolean checkConditions(Entity entity);

    /**
     * Override to add startup logic to the task
     * 
     * @param entity
     *            current entity
     */
    public void start(Entity entity) {
        this.entity = entity;
    }

    /**
     * Override to add ending logic to the task
     */
    public void end() {

    }

    /**
     * Override to specify the logic the task must update each cycle
     */
    public abstract boolean doAction(Entity entity);

    /**
     * Override to specify the controller the task has
     * 
     * @return The specific task controller.
     */
    public abstract TaskController getControl();
}
