package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.geom.Collision;

public class Cube {

    public enum CubeSideType {
        BACK,
        RIGHT,
        FRONT,
        LEFT,
        TOP,
        BOTTOM;

        public static CubeSideType getCorrectFace(int region, CubeSideType type) {
            switch (type) {
                case BACK:
                    switch (region) {
                        case 0:
                            return CubeSideType.BACK;
                        case 1:
                            return CubeSideType.LEFT;
                        case 2:
                            return CubeSideType.FRONT;
                        case 3:
                            return CubeSideType.RIGHT;
                    }
                    break;
                case LEFT:
                    switch (region) {
                        case 0:
                            return CubeSideType.LEFT;
                        case 1:
                            return CubeSideType.FRONT;
                        case 2:
                            return CubeSideType.RIGHT;
                        case 3:
                            return CubeSideType.BACK;
                    }
                    break;
                case FRONT:
                    switch (region) {
                        case 0:
                            return CubeSideType.FRONT;
                        case 1:
                            return CubeSideType.RIGHT;
                        case 2:
                            return CubeSideType.BACK;
                        case 3:
                            return CubeSideType.LEFT;
                    }
                    break;
                case RIGHT:
                    switch (region) {
                        case 0:
                            return CubeSideType.RIGHT;
                        case 1:
                            return CubeSideType.BACK;
                        case 2:
                            return CubeSideType.LEFT;
                        case 3:
                            return CubeSideType.FRONT;
                    }
                    break;
                case BOTTOM:
                    return CubeSideType.BOTTOM;
                case TOP:
                    return CubeSideType.TOP;
            }
            return type;
        }

    }

    public static final int QUADS = 6;

    private final CubeSide[] sides;
    private int position = 0;
    private boolean visible = false;
    private boolean hidden = false;

    public boolean corner;

    private final int region;
    private final int x;
    private final int y;
    private final Block type;

    public float scale = 1f;

    public Cube(int region, int x, int y, Block type) {
        this.region = region;
        this.x = x;
        this.y = y;
        this.type = type;
        this.sides = new CubeSide[QUADS];
    }

    public CubeSide[] getSides() {
        return sides;
    }

    public void addVerts(ArrayList<CubeVertex> quad, CubeSideType type) {
        sides[position++] = new CubeSide(quad, type);
    }

    public void setVisibleSide(int region, CubeSideType type, boolean visible) {
        sides[CubeSideType.getCorrectFace(region, type).ordinal()].setVisible(visible);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setVisible(int region, boolean visible) {
        setVisible(visible);
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

    public void setVisible2(int region, boolean visible) {
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

    public Vector3 min = new Vector3();
    public Vector3 mean = new Vector3();
    public Vector3 max = new Vector3();
    public Vector3 center = new Vector3();

    public void computeBoundaries() {
        min.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        max.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);

        for (CubeSide side : sides) {
            for (CubeVertex vert : side.getVerts()) {

                // x coord
                if (vert.getPosition()[0] < min.x) {
                    min.x = vert.getPosition()[0];
                } else if (vert.getPosition()[0] > max.x) {
                    max.x = vert.getPosition()[0];
                }

                // y coord
                if (vert.getPosition()[1] < min.y) {
                    min.y = vert.getPosition()[1];
                } else if (vert.getPosition()[1] > max.y) {
                    max.y = vert.getPosition()[1];
                }

                // z coord
                if (vert.getPosition()[2] < min.z) {
                    min.z = vert.getPosition()[2];
                } else if (vert.getPosition()[2] > max.z) {
                    max.z = vert.getPosition()[2];
                }
            }

        }

        mean.set((max.x + min.x) / 2f, (max.y + min.y) / 2f, (max.z + min.z) / 2f);
        center.set(min.x + mean.x, min.y + mean.y, min.z + mean.z);

    }

    public int getRegion() {
        return region;
    }

    public Block getType() {
        return type;
    }

    private static final Vector2 intersection = new Vector2(-1, -1);

    public boolean blocksRay(Cube c, Vector2 a, Vector2 b) {

        Vector2[] points = getCubeRoundPoint();
        ///  System.out.println(Arrays.toString(points));
        intersection.set(-1, -1);
        for (int i = 0; i < points.length; i++) {
            //     System.out.println(a + " : " + b);
            Intersector.intersectSegments(a, b, points[i], points[(i + 1) % points.length], intersection);
            if (intersection.x >= 0 || intersection.y >= 0) {
                if (Collision.inBetween(a, b, intersection)) {
                    //      System.out.println("Hit: " + intersection);
                    return true;
                }
            }

        }
        return false;
    }

    private boolean first = true;
    private boolean firstRound = true;
    private Vector2[][] points;
    private Vector2[][] four_points;

    private long timestamp;

    private boolean animation;

    private AnimationType animationType;

    public Vector2[] getCubePoint() {
        if (first) {
            points = new Vector2[App.getViews()][8];
            for (int i = 0; i < App.getViews(); i++) {
                CubeSideType face = CubeSideType.getCorrectFace(i, CubeSideType.BACK);
                CubeSide side = this.getSides()[face.ordinal()];
                Vector3[] nine = side.getNinePoints();
                for (int j = 0; j < nine.length; j++) {
                    points[i][j] = Collision.worldToScreen(i, nine[j]);
                }

            }
            first = false;
        }
        return points[this.getRegion()];
    }

    public Vector2[] getCubeRoundPoint() {
        if (firstRound) {
            four_points = new Vector2[App.getViews()][4];
            for (int i = 0; i < App.getViews(); i++) {
                CubeSideType face = CubeSideType.getCorrectFace(i, CubeSideType.BACK);
                CubeSide side = this.getSides()[face.ordinal()];
                Vector3[] four = side.getFourPoints();

                for (int j = 0; j < four.length; j++) {
                    four_points[i][j] = Collision.worldToScreen(i, four[j]);
                }
            }

        }
        firstRound = false;
        return four_points[this.getRegion()];
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        if (this.hidden != hidden) {
            animationType = hidden ? AnimationType.APPEAR : AnimationType.DISAPPEAR;
            timestamp = System.currentTimeMillis();
            this.hidden = hidden;
            change();
        } else {
            this.hidden = hidden;
        }
    }

    public enum AnimationType {
        APPEAR,
        DISAPPEAR;
    }

    private void change() {
        setVisible2(this.getRegion(), hidden);
        App.getCubeManager().checkCubeRegion(this);
    }

    private static final float ANIMATION_FACTOR = 100f;

    private float getAnimationCoeficient() {
        return Math.min(ANIMATION_FACTOR, ANIMATION_FACTOR / App.getPlayer().getMoveComponent().vector.len());
    }

    public boolean isAnimation() {
        return System.currentTimeMillis() - timestamp < getAnimationCoeficient();
    }

    public float getAnimationFactor() {
        return (System.currentTimeMillis() - timestamp) / getAnimationCoeficient();
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

}
