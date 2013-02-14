package com.kingx.dungeons;

import com.kingx.dungeons.trash.Actor;

public final class Logic implements Runnable {

    private static volatile Logic instance = null;

    private App reference;

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


    final float dt = 0.0166f;

    long currentTime = System.nanoTime();
    private static final long FORMAT = 1000000000;
    double accumulator = 0.0f;

    private boolean quit = false;

    @Override
    public void run() {
        while (!quit) {
            long newTime = System.nanoTime();
            double frameTime = (double) (newTime - currentTime) / FORMAT;
            currentTime = newTime;

            frameTime = Math.min(frameTime, 0.25); // note: max frame time to avoid spiral of death

            accumulator += frameTime;

            while (accumulator >= dt) {
                reference.update(dt);
                accumulator -= dt;
            }
        }
    }

    public static void move(Actor actor, float x, float y) {
        
    }

  
}
