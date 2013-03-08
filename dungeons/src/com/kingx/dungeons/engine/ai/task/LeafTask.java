package com.kingx.dungeons.engine.ai.task;

import com.kingx.dungeons.engine.ai.TaskController;

/**
 * Leaf task (or node) in the behavior tree.
 * 
 * Specifies a TaskControler, by composition, to take care of all the control logic, without burdening the Task class with complications.
 * 
 */
public abstract class LeafTask implements Task {
    /**
     * Task controler to keep track of the Task state.
     */
    protected TaskController control;

    /**
     * Creates a new instance of the LeafTask class
     * 
     * @param blackboard
     *            Reference to the AI Blackboard data
     */
    public LeafTask() {
        createController();
    }

    /**
     * Creates the controller for the class
     */
    private void createController() {
        this.control = new TaskController(this);
    }

    /**
     * Gets the controller reference.
     */
    @Override
    public TaskController getControl() {
        return this.control;
    }
}
