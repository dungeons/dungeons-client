package com.kingx.dungeons;

import com.artemis.World;

public final class Logic implements Runnable {

    // Singleton
    private static volatile Logic instance = null;

    private final World world;

    private final App app;

    private Logic(App app, World world) {
        this.app = app;
        this.world = world;
    }

    public static Logic getInstance(App app, World world) {
        if (instance == null) {
            synchronized (Logic.class) {
                if (instance == null) {
                    instance = new Logic(app, world);
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

                update(step);
                accumulator -= step;
            }
        }
    }

    private void update(float delta) {
        //System.out.println("UPDATE");
        world.setDelta(delta);
        world.process();
        //app.update(delta);
    }
}
