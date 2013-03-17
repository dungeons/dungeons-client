package com.kingx.dungeons.engine.ai.task;

import com.kingx.artemis.Entity;

/**
 * This parent task selects one of it's children to update.
 * 
 * To select a child, it starts from the beginning of it's children vector and
 * goes one by one until it finds one that passes the CheckCondition test. It
 * then updates that child until it finished. If the child finishes with
 * failure, it continues down the list looking another candidate to update, and
 * if it doesn't find it, it finishes with failure. If the child finishes with
 * success, the Selector considers it's task done and bails with success.
 * 
 */
public class Selector extends ParentTask {

    @Override
    public boolean doAction(Entity entity) {
        for (Task t : control.getSubtasks()) {
            t.start(entity);
            if (t.checkConditions(entity)) {
                if (t.doAction(entity)) {
                    return true;
                }
            }
        }
        return false;
    }
}
