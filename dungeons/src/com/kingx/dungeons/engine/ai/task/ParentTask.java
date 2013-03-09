package com.kingx.dungeons.engine.ai.task;

import com.artemis.Entity;
import com.kingx.dungeons.engine.ai.controller.ParentTaskController;
import com.kingx.dungeons.engine.ai.controller.TaskController;

/**
 * Inner node of the behavior tree, a flow director node, that selects a child
 * to be executed next.
 * 
 * Sets a specific kind of TaskController for these kinds of tasks.
 * 
 */
public abstract class ParentTask extends Task {
    /**
     * TaskControler for the parent task.
     */
    protected ParentTaskController control;

    public ParentTask() {
        createController();
    }

    /**
     * Creates the TaskController.
     */
    private void createController() {
        this.control = new ParentTaskController(this);
    }

    /**
     * Gets the control reference
     */
    @Override
    public TaskController getControl() {
        return control;
    }

    /**
     * Checks for the appropiate pre-state of the data
     */
    @Override
    public boolean checkConditions(Entity entity) {
        return control.getSubtasks().size() > 0;
    }
}
