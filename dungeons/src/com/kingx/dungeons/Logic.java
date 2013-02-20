package com.kingx.dungeons;

public final class Logic implements Runnable {

    // Singleton
    private static volatile Logic instance = null;

    private final App reference;

    private Logic(App app) {
        this.reference = app;
    }

    public static Logic getInstance(App app) {
        if (instance == null) {
            synchronized (Logic.class) {
                if (instance == null) {
                    instance = new Logic(app);
                    new Thread(instance).start();
                }
            }
        }
        return instance;
    }

    private final float step = .0166f;
    private final float maxStep = .25f;

    long currentTime = System.nanoTime();
    private final long BILLION = 1000000000;
    double accumulator = 0.0f;

    private final boolean quit = false;

    @Override
    public void run() {
        while (!quit) {
            long newTime = System.nanoTime();
            double frameTime = (double) (newTime - currentTime) / BILLION;
            currentTime = newTime;

            frameTime = Math.min(frameTime, maxStep); // note: max frame time to avoid spiral of death

            accumulator += frameTime;

            while (accumulator >= step) {
                reference.update(step);
                accumulator -= step;
            }
        }
    }
}
