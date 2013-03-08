package com.kingx.dungeons.engine.ai.task;

/**
 * This ParentTask executes each of it's children in turn until he has finished all of them.
 * 
 * It always starts by the first child, updating each one. If any child finishes with failure, the Sequence fails, and we finish with failure. When a
 * child finishes with success, we select the next child as the update victim. If we have finished updating the last child, the Sequence returns with
 * success.
 * 
 */
public class Sequence extends ParentTask {

    /**
     * A child finished with failure. We failed to update the whole sequence. Bail with failure.
     */
    @Override
    protected void childFailed() {
        control.FinishWithFailure();
    }

    /**
     * A child has finished with success Select the next one to update. If it's the last, we have finished with success.
     */
    @Override
    protected void childSucceeded() {
        int curPos = control.subtasks.indexOf(control.curTask);
        if (curPos == (control.subtasks.size() - 1)) {
            control.FinishWithSuccess();
        } else {
            control.curTask = control.subtasks.elementAt(curPos + 1);
            if (!control.curTask.checkConditions()) {
                control.FinishWithFailure();
            }
        }
    }
}
