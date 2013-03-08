package com.kingx.dungeons.engine.ai;

import java.util.Vector;

import com.kingx.dungeons.engine.ai.task.Task;

/**
 * This class extends the TaskController class to add support for child tasks and their logic. Used together with ParentTask.
 * 
 */
public class ParentTaskController extends TaskController {
    /**
     * Vector of child Task
     */
    public Vector<Task> subtasks;

    /**
     * Current updating task
     */
    public Task curTask;

    /**
     * Creates a new instance of the ParentTaskController class
     * 
     * @param task
     */
    public ParentTaskController(Task task) {
        super(task);

        this.subtasks = new Vector<Task>();
        this.curTask = null;
    }

    /**
     * Adds a new subtask to the end of the subtask list.
     * 
     * @param task
     *            Task to add
     */
    public void Add(Task task) {
        subtasks.add(task);
    }

    /**
     * Resets the task as if it had just started.
     */
    @Override
    public void Reset() {
        super.Reset();
        this.curTask = subtasks.firstElement();
    }
}
