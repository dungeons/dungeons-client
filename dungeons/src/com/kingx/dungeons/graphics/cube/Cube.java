package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

public class Cube {

    public enum CubeSideType {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    public static final int QUADS = 6;

    private final CubeSide[] sides;
    private int position = 0;
    private boolean visible = false;

    private boolean corner;

    public Cube() {
        this.sides = new CubeSide[QUADS];
    }

    public CubeSide[] getSides() {
        return sides;
    }

    public void addVerts(ArrayList<CubeVertex> quad) {
        sides[position++] = new CubeSide(quad);
    }

    public void setVisibleSide(int region, CubeSideType type, boolean visible) {
        switch (type) {

            case UP:
                sides[2].setVisible(visible);
                break;
            case DOWN:
                sides[0].setVisible(visible);
                break;
            case LEFT:
                switch (region) {
                    case 0:
                        sides[3].setVisible(visible);
                        break;
                    case 1:
                        sides[4].setVisible(visible);
                        break;
                    case 2:
                        sides[1].setVisible(visible);
                        break;
                    case 3:
                        sides[5].setVisible(visible);
                        break;
                }
            case RIGHT:
                switch (region) {
                    case 0:
                        sides[1].setVisible(visible);
                        break;
                    case 1:
                        sides[5].setVisible(visible);
                        break;
                    case 2:
                        sides[3].setVisible(visible);
                        break;
                    case 3:
                        sides[4].setVisible(visible);
                        break;
                }
            case TOP:
                switch (region) {
                    case 0:
                        sides[4].setVisible(visible);
                        break;
                    case 1:
                        sides[1].setVisible(visible);
                        break;
                    case 2:
                        sides[5].setVisible(visible);
                        break;
                    case 3:
                        sides[3].setVisible(visible);
                        break;
                }
            case BOTTOM:
                switch (region) {
                    case 0:
                        sides[5].setVisible(visible);
                        break;
                    case 1:
                        sides[3].setVisible(visible);
                        break;
                    case 2:
                        sides[4].setVisible(visible);
                        break;
                    case 3:
                        sides[1].setVisible(visible);
                        break;
                }

        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(int region, boolean visible) {
        this.visible = visible;

        if (visible) {

            setVisibleSide(region, CubeSideType.UP, true);
            setVisibleSide(region, CubeSideType.DOWN, true);
            setVisibleSide(region, CubeSideType.LEFT, true);
            setVisibleSide(region, CubeSideType.RIGHT, true);
            setVisibleSide(region, CubeSideType.TOP, true);
            setVisibleSide(region, CubeSideType.BOTTOM, corner);
        } else {
            setVisibleSide(region, CubeSideType.UP, false);
            setVisibleSide(region, CubeSideType.DOWN, false);
            setVisibleSide(region, CubeSideType.LEFT, false);
            setVisibleSide(region, CubeSideType.RIGHT, false);
            setVisibleSide(region, CubeSideType.TOP, false);
            setVisibleSide(region, CubeSideType.BOTTOM, !corner);

        }

    }

    public void setCorner(boolean corner) {
        this.corner = corner;
    }

}
