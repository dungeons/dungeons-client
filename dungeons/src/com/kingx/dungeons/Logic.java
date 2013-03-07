package com.kingx.dungeons;

import com.artemis.World;

public final class Logic implements Runnable {

    // Singleton
    private static volatile Logic instance = null;

    private final World world;

    private Logic(World world) {
        this.world = world;
    }

    public static void init(World world) {
        if (instance == null) {
            instance = new Logic(world);
            new Thread(instance).start();
        }
    }

    private final long BILLION = 1000000000;
    private long currentTime = System.nanoTime();

    private final float step = .0166f;
    private final float maxStep = .25f;
    private float accumulator = 0.0f;

    private final boolean quit = false;

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

    private void update(float delta) {
        world.setDelta(delta);
        world.process();
    }
}
