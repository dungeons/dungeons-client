package com.kingx.dungeons.utils;

/**
 * Time step class, for regulating update speed.
 */
public class Ticker {
    /** Number of ticks between updates. */
    private final int maxCount;
    /** Whether or not to repeat updates. */
    private final boolean repeat;
    /** Current tick count */
    private int count = 0;

    /**
     * Creates a new instance of the Ticker class
     * 
     * @param maxCount
     *            number of ticks between updates
     */
    public Ticker(int maxCount, boolean repeat) {
        this.maxCount = maxCount;
        this.repeat = repeat;
        reset();
    }

    /**
     * Returns true if the ticker is ready for a update, false otherwise
     * 
     * @return {@code true} if ticker is ready, {@code false} otherwise.
     */
    public boolean isReady() {
        tick();
        boolean ready = count > maxCount;
        if (repeat) {
            reset();
        }
        return ready;
    }

    /**
     * One tick cycle.
     */
    private void tick() {
        count++;
    }

    /**
     * Resets the ticker.
     */
    public void reset() {
        count = 0;
    }
}
