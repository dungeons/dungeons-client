package com.kingx.dungeons.android;

import android.os.Debug;

import com.kingx.dungeons.App;

public class AndroidApp extends App {

    private static final boolean DEBUG = false;
    private static final int BUFFER = 52428800;
    private int trace = 0;

    public AndroidApp() {
        super(null);
    }

    @Override
    public void create() {
        super.create();
        if (DEBUG) {
            Debug.startMethodTracing("dungeons_" + trace, BUFFER);
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (DEBUG) {
            Debug.stopMethodTracing();
            trace++;
        }
    }

    @Override
    public void resume() {
        super.resume();
        if (DEBUG) {
            Debug.startMethodTracing("dungeons_" + trace);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (DEBUG) {
            Debug.stopMethodTracing();
        }
    }

}
