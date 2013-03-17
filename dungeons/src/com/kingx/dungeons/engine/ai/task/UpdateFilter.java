package com.kingx.dungeons.engine.ai.task;

import com.kingx.artemis.Entity;
import com.kingx.dungeons.utils.Ticker;

/**
 * Decorator that adds a update speed limit to the task it is applied to
 * 
 */
public class UpdateFilter extends TaskDecorator {
    /**
     * Ticker to keep track of ticks
     */
    private final Ticker ticker;
    /**
     * Number of ticks between updates.
     */
    private int updateTicks;

    /**
     * Creates a new instance of the RegulatorDecorator class
     * 
     * @param task
     *            Task to decorate
     * @param updateTicks
     *            Ticks between each frame update
     */
    public UpdateFilter(Task task, int updateTicks) {
        super(task);
        this.updateTicks = updateTicks;
        this.ticker = new Ticker(updateTicks, false);
    }

    /**
     * Starts the task and the regulator
     * 
     * @param entity
     *            current entity
     */
    @Override
    public void start(Entity entity) {
        task.start(entity);
    }

    /**
     * Updates the decorated task only if the required time since the last
     * update has elapsed.
     */
    @Override
    public boolean doAction(Entity entity) {
        if (this.ticker.isReady()) {
            task.doAction(entity);
        }
        return false;
    }

    /**
     * Set number of ticks between updates.
     * 
     * @param updateTicks
     *            number of ticks
     */
    public void setUpdateTicks(int updateTicks) {
        this.updateTicks = updateTicks;
    }

}