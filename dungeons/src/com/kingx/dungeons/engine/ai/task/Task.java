package com.kingx.dungeons.engine.ai.task;

import com.kingx.dungeons.engine.ai.TaskController;

public interface Task {

    /**
     * Override to do a pre-conditions check to see if the task can be updated.
     * 
     * @return True if it can, false if it can't
     */
    boolean checkConditions();

    /**
     * Override to add startup logic to the task
     */
    void start();

    /**
     * Override to add ending logic to the task
     */
    void end();

    /**
     * Override to specify the logic the task must update each cycle
     */
    void doAction();

    /**
     * Override to specify the controller the task has
     * 
     * @return The specific task controller.
     */
    TaskController getControl();
}
