package com.kingx.dungeons.graphics.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.kingx.dungeons.App;

public class NinePath {

    private final ShapeRenderer renderer;

    private final Region up;
    private final Region down;
    private final Region left;
    private final Region right;
    private final Region middle;

    private static final float REGION_SIZE = 3f;

    NinePath(int x, int y, int width, int height, ShapeRenderer renderer, int[] keys) {
        float w = width / REGION_SIZE;
        float h = height / REGION_SIZE;
        this.renderer = renderer;

        up = Region.getInstance(x + w, y + 2 * h, w, h, keys[0]);
        down = Region.getInstance(x + w, y, w, h, keys[1]);
        left = Region.getInstance(x, y + h, w, h, keys[2]);
        right = Region.getInstance(x + w * 2, y + h, w, h, keys[3]);
        middle = Region.getInstance(x + w, y + h, w, h, -1);
    }

    void drawUp(NinePathType type) {
        draw(up, type);
    }

    void drawDown(NinePathType type) {
        draw(down, type);
    }

    void drawLeft(NinePathType type) {
        draw(left, type);
    }

    void drawRight(NinePathType type) {
        draw(right, type);
    }

    void drawMiddle(NinePathType type) {
        draw(middle, type);
    }

    void draw(Region region, NinePathType type) {
        switch (type) {
            case BOX:
                renderer.rect(region.x, region.y, region.width, region.height);
                break;
            case CIRCLE:
                float half = region.width / 2f;
                renderer.circle(region.x + half, region.y + half, half);
                break;
        }
    }

    public static void hit(float x, float y, int index) {
        Region.pressAll(x, y, index);
    }

    public static void release(int index) {
        Region.releaseAll(index);
    }

    static class Region extends Rectangle {

        /**
         * 
         */
        private static final long serialVersionUID = 649546877263222944L;

        private static List<Region> regions = new ArrayList<Region>();
        int current = -1;
        private final int keycode;

        public static Region getInstance(float x, float y, float width, float height, int keycode) {
            Region instace = new Region(x, y, width, height, keycode);
            regions.add(instace);
            return instace;
        }

        public Region(float x, float y, float width, float height, int keycode) {
            super(x, y, width, height);
            this.keycode = keycode;
        }

        public int getCurrent() {
            return current;
        }

        public void release(int i) {
            if (i == current) {
                App.getInput().keyUp(keycode);
                current = -1;
            }
        }

        public static void releaseAll(int i) {
            for (Region region : regions) {
                region.release(i);
            }
        }

        public static void pressAll(float x, float y, int index) {
            for (Region region : regions) {
                region.isHit(x, y, index);
            }
        }

        public boolean isHit(float x, float y, int index) {
            if (x > this.x && x < this.x + this.width) {
                if (y > this.y && y < this.y + this.height) {
                    if (current != index) {
                        current = index;
                        App.getInput().keyDown(keycode);
                    }
                    return true;
                }
            }
            return false;
        }
    }
}