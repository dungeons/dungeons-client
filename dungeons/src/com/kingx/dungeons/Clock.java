package com.kingx.dungeons;

import com.kingx.dungeons.server.Server;

public class Clock implements Runnable {

    private volatile boolean init = true;
    private final boolean quit = false;
    private Server server;

    public void init(Server server) {
        this.server = server;
        if (init) {
            new Thread(this).start();
            init = false;
        }
    }

    private final long BILLION = 1000000000;
    private long currentTime = System.nanoTime();

    private final float delta = .0166f;
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

            if (accumulator < delta) {
                if (!App.NOSLEEP) {
                    try {
                        // Takes a nap for the remaining time.
                        Thread.sleep((long) ((delta - accumulator) * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                while (accumulator >= delta) {
                    server.update(delta);
                    accumulator -= delta;
                }
            }
        }
    }
}
