package com.kingx.dungeons.engine.ai.task;

import com.kingx.dungeons.engine.ai.ParentTaskController;
import com.kingx.dungeons.engine.ai.TaskController;

/**
 * Inner node of the behavior tree, a flow director node, that selects a child to be executed next.
 * 
 * Sets a specific kind of TaskController for these kinds of tasks.
 * 
 */
public abstract class ParentTask implements Task {
    /**
     * TaskControler for the parent task
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
    public boolean checkConditions() {
        return control.subtasks.size() > 0;
    }

    /**
     * Abstract to be overridden in child classes. Called when a child finishes with success.
     */
    protected abstract void childSucceeded();

    /**
     * Abstract to be overridden in child classes. Called when a child finishes with failure.
     */
    protected abstract void childFailed();

    /**
     * Checks whether the child has started, finished or needs updating, and takes the needed measures in each case
     */
    @Override
    public void doAction() {
        if (control.Finished()) {
            return;
        }
        if (control.curTask == null) {
            // If there is a null task, we've done something wrong
            System.err.println("Selector Current task has a null action");
            return;
        }

        // If we do have a curTask...
        if (!control.curTask.getControl().Started()) {
            // ... and it's not started yet, start it.
            control.curTask.getControl().SafeStart();
        } else if (control.curTask.getControl().Finished()) {
            // ... and it's finished, end it properly.
            control.curTask.getControl().SafeEnd();

            if (control.curTask.getControl().Succeeded()) {
                this.childSucceeded();
            }
            if (control.curTask.getControl().Failed()) {
                this.childFailed();
            }
        } else {
            // ... and it's ready, update it.		
            control.curTask.doAction();
        }
    }

    /**
     * Ends the task
     */
    @Override
    public void end() {
        System.out.println("Ending");
    }

    /**
     * Starts the task, and points the current task to the first one of the available child tasks.
     */
    @Override
    public void start() {
        System.out.println("Starting");
        control.curTask = control.subtasks.firstElement();
        if (control.curTask == null) {
            System.err.println("Selector Current task has a null action");
        }
    }
}
