package com.kingx.dungeons.engine.ai.task;

import com.kingx.artemis.Entity;

/**
 * This ParentTask executes each of it's children in turn until he has finished
 * all of them.
 * 
 * It always starts by the first child, updating each one. If any child finishes
 * with failure, the Sequence fails, and we finish with failure. When a child
 * finishes with success, we select the next child as the update victim. If we
 * have finished updating the last child, the Sequence returns with success.
 * 
 */
public class Sequence extends ParentTask {

    @Override
    public boolean doAction(Entity entity) {
        for (Task t : control.getSubtasks()) {
            t.start(entity);
            if (!t.checkConditions(entity) || !t.doAction(entity)) {
                return false;
            }
        }
        return true;
    }
}
