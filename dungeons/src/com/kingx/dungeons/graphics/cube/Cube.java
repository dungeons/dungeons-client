package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

public class Cube {

    public enum CubeSideType {
        BACK,
        RIGHT,
        FRONT,
        LEFT,
        TOP,
        BOTTOM
    }

    public static final int QUADS = 6;

    private final CubeSide[] sides;
    private int position = 0;
    private boolean visible = false;

    private boolean corner;

    private final int x;
    private final int y;

    public Cube(int x, int y) {
        this.x = x;
        this.y = y;
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

            case BACK:
                switch (region) {
                    case 0:
                        sides[CubeSideType.BACK.ordinal()].setVisible(visible);
                        break;
                    case 1:
                        sides[CubeSideType.LEFT.ordinal()].setVisible(visible);
                        break;
                    case 2:
                        sides[CubeSideType.FRONT.ordinal()].setVisible(visible);
                        break;
                    case 3:
                        sides[CubeSideType.RIGHT.ordinal()].setVisible(visible);
                        break;
                }
            case LEFT:
                switch (region) {
                    case 0:
                        sides[CubeSideType.LEFT.ordinal()].setVisible(visible);
                        break;
                    case 1:
                        sides[CubeSideType.FRONT.ordinal()].setVisible(visible);
                        break;
                    case 2:
                        sides[CubeSideType.RIGHT.ordinal()].setVisible(visible);
                        break;
                    case 3:
                        sides[CubeSideType.BACK.ordinal()].setVisible(visible);
                        break;
                }
            case FRONT:
                switch (region) {
                    case 0:
                        sides[CubeSideType.FRONT.ordinal()].setVisible(visible);
                        break;
                    case 1:
                        sides[CubeSideType.RIGHT.ordinal()].setVisible(visible);
                        break;
                    case 2:
                        sides[CubeSideType.BACK.ordinal()].setVisible(visible);
                        break;
                    case 3:
                        sides[CubeSideType.LEFT.ordinal()].setVisible(visible);
                        break;
                }
            case RIGHT:
                switch (region) {
                    case 0:
                        sides[CubeSideType.RIGHT.ordinal()].setVisible(visible);
                        break;
                    case 1:
                        sides[CubeSideType.BACK.ordinal()].setVisible(visible);
                        break;
                    case 2:
                        sides[CubeSideType.LEFT.ordinal()].setVisible(visible);
                        break;
                    case 3:
                        sides[CubeSideType.FRONT.ordinal()].setVisible(visible);
                        break;
                }
            case BOTTOM:
                sides[CubeSideType.BOTTOM.ordinal()].setVisible(visible);
            case TOP:
                sides[CubeSideType.TOP.ordinal()].setVisible(visible);

        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(int region, boolean visible) {
        this.visible = visible;
        if (visible) {
            setVisibleSide(region, CubeSideType.BACK, corner);
            setVisibleSide(region, CubeSideType.LEFT, true);
            setVisibleSide(region, CubeSideType.FRONT, true);
            setVisibleSide(region, CubeSideType.RIGHT, true);
            setVisibleSide(region, CubeSideType.BOTTOM, true);
            setVisibleSide(region, CubeSideType.TOP, true);
        } else {
            setVisibleSide(region, CubeSideType.BACK, !corner);
            setVisibleSide(region, CubeSideType.LEFT, false);
            setVisibleSide(region, CubeSideType.FRONT, false);
            setVisibleSide(region, CubeSideType.RIGHT, false);
            setVisibleSide(region, CubeSideType.BOTTOM, false);
            setVisibleSide(region, CubeSideType.TOP, false);
        }
    }

    public void setCorner(boolean corner) {
        this.corner = corner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
