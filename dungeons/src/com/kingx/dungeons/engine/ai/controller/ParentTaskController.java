package com.kingx.dungeons.engine.ai.controller;

import java.util.LinkedList;
import java.util.List;

import com.kingx.dungeons.engine.ai.task.Task;

/**
 * This class extends the TaskController class to add support for child tasks
 * and their logic. Used together with ParentTask.
 * 
 */
public class ParentTaskController extends TaskController {
    /**
     * Vector of child Task
     */
    private final List<Task> subtasks;

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

        this.subtasks = new LinkedList<Task>();
        this.curTask = null;
    }

    /**
     * Adds a new subtask to the end of the subtask list.
     * 
     * @param task
     *            Task to add
     */
    public void add(Task task) {
        subtasks.add(task);
    }

    /**
     * Resets the task as if it had just started.
     */
    @Override
    public void reset() {
        super.reset();
    }

    /**
     * Returns all subtasks in this node.
     * 
     * @return subtasks in current node
     */
    public List<Task> getSubtasks() {
        return subtasks;
    }

}
