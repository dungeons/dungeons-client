package com.kingx.dungeons.engine.ai.task;

/**
 * This parent task selects one of it's children to update.
 * 
 * To select a child, it starts from the beginning of it's children vector and goes one by one until it finds one that passes the CheckCondition test. It
 * then updates that child until it finished. If the child finishes with failure, it continues down the list looking another candidate to update, and if
 * it doesn't find it, it finishes with failure. If the child finishes with success, the Selector considers it's task done and bails with success.
 * 
 */
public class Selector extends ParentTask {

    /**
     * Chooses the new task to update.
     * 
     * @return The new task, or null if none was found
     */
    private Task chooseNewTask() {
        Task task = null;
        boolean found = false;
        int curPos = control.subtasks.indexOf(control.curTask);

        while (!found) {
            if (curPos == (control.subtasks.size() - 1)) {
                found = true;
                task = null;
                break;
            }

            curPos++;

            task = control.subtasks.elementAt(curPos);
            if (task.checkConditions()) {
                found = true;
            }
        }

        return task;
    }

    /**
     * In case of child finishing with failure we find a new one to update, or fail if none is to be found
     */
    @Override
    protected void childFailed() {
        control.curTask = chooseNewTask();
        if (control.curTask == null) {
            control.FinishWithFailure();
        }
    }

    /**
     * In case of child finishing with sucess, our job here is done, finish with sucess as well
     */
    @Override
    protected void childSucceeded() {
        control.FinishWithSuccess();
    }
}
