package com.kingx.dungeons.engine.ai.task;

import com.kingx.artemis.Entity;

/**
 * Selector is logical <tt>OR</tt> over child task.
 * 
 * Loops through all children one by one and selects first that returns
 * {@code true} on conditions. Selector fails if all children fail.
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
