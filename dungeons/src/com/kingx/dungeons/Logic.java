package com.kingx.dungeons;

public abstract class Logic implements Runnable {

    private volatile boolean init = true;
    private final boolean quit = false;

    public void init() {
        if (init) {
            new Thread(this).start();
            init = false;
        }
    }

    private final long BILLION = 1000000000;
    private long currentTime = System.nanoTime();

    private final float step = .0166f;
    private final float maxStep = .25f;
    private float accumulator = 0.0f;

    @Override
    public void run() {
        while (!quit) {
            long newTime = System.nanoTime();
            double frameTime = (double) (newTime - currentTime) / BILLION;
            currentTime = newTime;

            frameTime = Math.min(frameTime, maxStep); // note: max frame time to avoid spiral of death

            accumulator += frameTime;

            if (accumulator < step) {
                if (!App.NOSLEEP) {
                    try {
                        // Takes a nap for the remaining time.
                        Thread.sleep((long) ((step - accumulator) * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                while (accumulator >= step) {
                    update(step);
                    accumulator -= step;
                }
            }
        }
    }

    protected abstract void update(float delta);
}
