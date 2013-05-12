package com.kingx.dungeons.geom;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.kingx.dungeons.App;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Point.Int;
import com.kingx.dungeons.graphics.cube.Cube;
import com.kingx.dungeons.graphics.cube.CubeRegion;

/**
 * This class contains method coppied and modified from {@link Intersector}
 * class. Standard methods from there are using public static
 * {@link Vector3#tmp} properties that are accessed from all over the API. When
 * run in parallel thread, synchronization issues arise.
 * 
 * @author xkings
 * 
 */
public class Collision {

    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp1 = new Vector3();
    private static Vector3 tmp2 = new Vector3();
    private static Vector3 tmp3 = new Vector3();

    /**
     * {@inheritDoc}
     * 
     * <pre>
     * This method is optimized.
     * </pre>
     */
    public static boolean intersectRayTrianglesBetweenPoints(Ray ray, float[] triangles, Vector3 a, Vector3 b) {
        if ((triangles.length / 3) % 3 != 0) {
            throw new RuntimeException("triangle list size is not a multiple of 3");
        }

        for (int i = 0; i < triangles.length - 6; i += 9) {
            boolean result = intersectRayTriangle(ray, tmp1.set(triangles[i], triangles[i + 1], triangles[i + 2]),
                    tmp2.set(triangles[i + 3], triangles[i + 4], triangles[i + 5]), tmp3.set(triangles[i + 6], triangles[i + 7], triangles[i + 8]), tmp);

            if (result) {
                if (inBetween(a, b, tmp)) {
                    return true;
                }
            }
        }
        return false;

    }

    private static Vector3 tmp4 = new Vector3();

    private static Vector3 tmp(Vector3 v) {
        return tmp4.set(v);
    }

    /**
     * <pre>
     * This method is modified so it does not use public static
     * {@link Vector3#tmp} property. No synchronization issues.
     * </pre>
     * 
     * @param ray
     *            The ray
     * @param plane
     *            The plane
     * @param intersection
     *            The vector the intersection point is written to (optional)
     * @return Whether an intersection is present.
     * @see Intersector#intersectRayPlane(Ray ray, Plane plane, Vector3
     *      intersection)
     */
    public static boolean intersectRayPlane(Ray ray, Plane plane, Vector3 intersection) {
        float denom = ray.direction.dot(plane.getNormal());
        if (denom != 0) {
            float t = -(ray.origin.dot(plane.getNormal()) + plane.getD()) / denom;
            if (t < 0) {
                return false;
            }

            if (intersection != null) {
                intersection.set(ray.origin).add(tmp(ray.direction).mul(t));
            }
            return true;
        } else if (plane.testPoint(ray.origin) == Plane.PlaneSide.OnPlane) {
            if (intersection != null) {
                intersection.set(ray.origin);
            }
            return true;
        } else {
            return false;
        }
    }

    private static final MyPlane p = new MyPlane(new Vector3(), 0);
    private static final Vector3 i = new Vector3();

    private final static Vector3 v0 = new Vector3();
    private final static Vector3 v1 = new Vector3();
    private final static Vector3 v2 = new Vector3();

    /**
     * <pre>
     * This method is modified so it does not use public static
     * {@link Vector3#tmp} property. No synchronization issues.
     * </pre>
     * 
     * @param ray
     *            The ray
     * @param t1
     *            The first vertex of the triangle
     * @param t2
     *            The second vertex of the triangle
     * @param t3
     *            The third vertex of the triangle
     * @param intersection
     *            The intersection point (optional)
     * 
     * @see Intersector#intersectRayTriangle(Ray, Vector3, Vector3, Vector3,
     *      Vector3)
     */
    public static boolean intersectRayTriangle(Ray ray, Vector3 t1, Vector3 t2, Vector3 t3, Vector3 intersection) {
        p.set(t1, t2, t3);
        if (!intersectRayPlane(ray, p, i)) {
            return false;
        }

        v0.set(t3).sub(t1);
        v1.set(t2).sub(t1);
        v2.set(i).sub(t1);

        float dot00 = v0.dot(v0);
        float dot01 = v0.dot(v1);
        float dot02 = v0.dot(v2);
        float dot11 = v1.dot(v1);
        float dot12 = v1.dot(v2);

        float denom = dot00 * dot11 - dot01 * dot01;
        if (denom == 0) {
            return false;
        }

        float u = (dot11 * dot02 - dot01 * dot12) / denom;
        float v = (dot00 * dot12 - dot01 * dot02) / denom;

        if (u >= 0 && v >= 0 && u + v <= 1) {
            if (intersection != null) {
                intersection.set(i);
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks if {@code c} is in a square delimited by {@code a} and {@code b}
     * 
     * @param a
     *            point a
     * @param b
     *            point b
     * @param c
     *            point c
     * @return {@code true} if point {@code c} is between {@code a} and
     *         {@code b}, {@code false} otherwise
     */
    public static boolean inBetween(Vector3 a, Vector3 b, Vector3 c) {
        float minx = Math.min(a.x, b.x);
        float miny = Math.min(a.y, b.y);
        float maxx = Math.max(a.x, b.x);
        float maxy = Math.max(a.y, b.y);
        return c.x >= minx && c.x <= maxx && c.y >= miny && c.y <= maxy;
    }

    public static boolean inBetween(Vector2 a, Vector2 b, Vector2 c) {
        float minx = Math.min(a.x, b.x);
        float miny = Math.min(a.y, b.y);
        float maxx = Math.max(a.x, b.x);
        float maxy = Math.max(a.y, b.y);
        return c.x >= minx && c.x <= maxx && c.y >= miny && c.y <= maxy;
    }

    private static class MyPlane extends Plane {
        private static final long serialVersionUID = -1240652082930747866L;

        public MyPlane(Vector3 normal, float d) {
            super(normal, d);
        }

        private final static Vector3 tmp = new Vector3();
        private final static Vector3 tmp2 = new Vector3();

        private Vector3 tmp(Vector3 v) {
            return tmp.set(v);
        }

        private Vector3 tmp2(Vector3 v) {
            return tmp2.set(v);
        }

        /**
         * {@inheritDoc}
         * 
         * <pre>
         * This method is modified so it does not use public static
         * {@link Vector3#tmp} property. No synchronization issues.
         * </pre>
         */
        @Override
        public void set(Vector3 point1, Vector3 point2, Vector3 point3) {
            Vector3 l = tmp(point1).sub(point2);
            Vector3 r = tmp2(point2).sub(point3);
            Vector3 nor = l.crs(r).nor();
            normal.set(nor);
            d = -point1.dot(nor);
        }

    }

    /**
     * Returns <tt>xy</tt> distance between two points.
     * 
     * @param a
     *            point A
     * @param b
     *            point B
     * @return distance between points
     */
    public static float distance(Vector3 a, Vector3 b) {
        float tx = a.x - b.x;
        float ty = a.y - b.y;
        return (float) Math.sqrt(tx * tx + ty * ty);
    }

    /**
     * Can {@code a} see {@code b} ?
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean canSee(Vector3 a, Vector3 b, float radius) {
        throw new IllegalArgumentException("Not implemented yet.");
        /*Ray ray = getRay(a, b);
        if (distance(a, b) <= radius) {
            // FIXME maze 0
            if (!intersectRayTrianglesBetweenPoints(ray, App.getMaze(0).getVerts(), a, b)) {
                return true;
            }
        }
        return false;*/
    }

    private static Ray getRay(Vector3 a, Vector3 b) {
        return new Ray(a, b.cpy().sub(a));
    }

    public static void correct(Vector3 position, float offset) {
        float borderOffset = 1 - offset;

        position.x = Math.max(position.x, CubeRegion.min.x + borderOffset);
        position.x = Math.min(position.x, CubeRegion.max.x - borderOffset);

        position.z = Math.min(position.z, CubeRegion.max.z - borderOffset);
        position.z = Math.max(position.z, CubeRegion.min.z + borderOffset);
    }

    /**
     * Checks for collision on given position. If object is entity is colliding,
     * it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param size
     *            size of entity
     * @return {@code true} whether there were collision, {@code false}
     *         otherwise
     */
    public static Int resolveCollisionUp(PositionComponent position, SizeComponent size) {

        float halfSize = size.getSize() / 2f;
        float x = worldToScreen(position.inWorld).x;
        float y = position.getY();

        float upBound = y + halfSize;

        int clampedValue = (int) (upBound / App.UNIT);

        // Edges are not colliding
        if (clampedValue != upBound) {
            Int upPoint = new Point.Int((int) (x / App.UNIT), (int) (upBound / App.UNIT));

            if (!isWalkable(upPoint, true)) {
                y = upPoint.y * App.UNIT - halfSize;
                position.setY(y);
                return upPoint;
            }

        }
        return null;
    }

    /**
     * Checks for collision on given position. If object is entity is colliding,
     * it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param size
     *            size of entity
     * @return {@code true} whether there were collision, {@code false}
     *         otherwise
     */
    public static Int resolveCollisionDown(PositionComponent position, SizeComponent size) {

        float halfSize = size.getSize() / 2f;
        float x = worldToScreen(position.inWorld).x;
        float y = position.getY();

        float downBound = y - halfSize;

        int clampedValue = (int) (downBound / App.UNIT);

        // Edges are not colliding
        if (clampedValue != downBound) {
            Int downPoint = new Point.Int((int) (x / App.UNIT), (int) (downBound / App.UNIT));

            if (!isWalkable(downPoint, true)) {
                y = (downPoint.y + 1) * App.UNIT + halfSize;
                position.setY(y);
                return downPoint;
            }

        }
        return null;
    }

    /**
     * Checks for collision on given position. If object is entity is colliding,
     * it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param size
     *            size of entity
     * @return {@code true} whether there were collision, {@code false}
     *         otherwise
     */
    public static Int resolveCollisionLeft(PositionComponent position, SizeComponent size) {

        float halfSize = size.getSize() / 2f;
        float x = worldToScreen(position.inWorld).x;
        float y = position.getY();

        float leftBound = x - halfSize;

        int clampedValue = (int) (leftBound / App.UNIT);

        // Edges are not colliding
        if (clampedValue != leftBound) {
            Int leftPoint = new Point.Int((int) (leftBound / App.UNIT), (int) (y / App.UNIT));

            if (!isWalkable(leftPoint, true)) {
                x = (leftPoint.x + 1) * App.UNIT + halfSize;
                screenToWorld(x, y, position.inWorld);
                return leftPoint;
            }

        }
        return null;
    }

    /**
     * Checks for collision on given position. If object is entity is colliding,
     * it is pushed to the other side
     * 
     * @param position
     *            current position of entity
     * @param size
     *            size of entity
     * @return {@code true} whether there were collision, {@code false}
     *         otherwise
     */
    public static Int resolveCollisionRight(PositionComponent position, SizeComponent size) {

        float halfSize = size.getSize() / 2f;
        float x = worldToScreen(position.inWorld).x;
        float y = position.getY();

        float rightBound = x + halfSize;

        int clampedValue = (int) (rightBound / App.UNIT);

        // Edges are not colliding
        if (clampedValue != rightBound) {
            Int rightPoint = new Point.Int((int) (rightBound / App.UNIT), (int) (y / App.UNIT));

            if (!isWalkable(rightPoint, true)) {
                x = rightPoint.x * App.UNIT - halfSize;
                screenToWorld(x, y, position.inWorld);
                return rightPoint;
            }

        }

        return null;
    }

    /**
     * Check whether is point in map bounds and walkable
     * 
     * @param point
     *            to be checked
     * @return {@code true} if point is walkable, {@code false} otherwise
     */
    public static boolean isWalkable(Int point, boolean offBounds) {
        BlockPair[][] footprint = App.getTerrain().getFootprint();
        point = point.cpy();
        if (point.x == footprint.length) {
            footprint = App.getTerrain().getNextFootprint();
            point.x = 0;
        }

        if (point.x < 0 || point.x >= footprint.length + 1) {
            return offBounds;
        }

        if (point.y < 0 || point.y >= footprint[0].length) {
            return offBounds;
        }

        return footprint[point.x][point.y].isRemoved();
    }

    public static Vector2 worldToScreen(Vector3 world) {
        return worldToScreen(App.getCurrentView(), world, new Vector2());
    }

    public static Vector2 worldToScreen(int view, Vector3 world) {
        return worldToScreen(view, world, new Vector2());
    }

    public static Vector2 worldToScreen(Vector3 world, Vector2 screen) {
        return worldToScreen(App.getCurrentView(), world, screen);
    }

    public static Vector2 worldToScreen(int view, Vector3 world, Vector2 screen) {
        screen.set(world.x, world.y);
        switch (App.getCurrentView()) {
            case 0:
                screen.x = world.x + CubeRegion.min.x;
                screen.y = world.y;
                break;
            case 1:
                screen.x = -world.z + CubeRegion.max.z;
                screen.y = world.y;
                break;
            case 2:
                screen.x = CubeRegion.max.x - world.x;
                screen.y = world.y;
                break;
            case 3:
                screen.x = world.z - CubeRegion.min.z;
                screen.y = world.y;
                break;
        }
        return screen;
    }

    public static void screenToWorld(float x, float y, Vector3 world) {
        switch (App.getCurrentView()) {
            case 0:
                world.x = x + CubeRegion.min.x;
                world.z = CubeRegion.max.z;
                break;
            case 1:
                world.x = CubeRegion.max.x;
                world.z = -x + CubeRegion.max.z;
                break;
            case 2:
                world.x = -x + CubeRegion.max.x;
                world.z = CubeRegion.min.z;
                break;
            case 3:
                world.x = CubeRegion.min.x;
                world.z = x + CubeRegion.min.z;
                break;
        }
        world.y = y;
    }

    public static Vector3 screenToWorld(Vector2 screen) {
        Vector3 result = new Vector3();
        switch (App.getCurrentView()) {
            case 0:
                result.x = screen.x + CubeRegion.min.x;
                result.z = CubeRegion.max.z;
                break;
            case 1:
                result.x = CubeRegion.max.x;
                result.z = -screen.x + CubeRegion.max.z;
                break;
            case 2:
                result.x = -screen.x + CubeRegion.max.x;
                result.z = CubeRegion.min.z;
                break;
            case 3:
                result.x = CubeRegion.min.x;
                result.z = screen.x + CubeRegion.min.z;
                break;
        }
        result.y = screen.y;
        return result;
    }

    public static boolean isCubeVisible(Cube cube, int radius) {
        Int point = App.getPlayer().getCollision().getCurrent();
        PositionComponent pos = App.getPlayer().getPositionComponent();
        Vector2 avatar = pos.getScreen();

        if (point == null) {
            return false;
        }

        if (cube.getY() == App.getTerrain().getHeight() - 1) {
            return true;
        }

        if (cube != null) {
            // TODO optimalization
            if (App.getCubeManager().isCubeSurrounded(cube)) {
                return false;
            }
            if (cube.getX() >= point.x - radius && cube.getX() <= point.x + radius) {
                if (cube.getY() >= point.y - radius && cube.getY() <= point.y + radius) {
                    Vector2[] points = cube.getCubePoint();
                    for (int i = 0; i < points.length; i++) {
                        if (fireLight(cube, avatar, points[i], radius)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean fireLight(Cube cube, Vector2 from, Vector2 to, int radius) {
        Int point = App.getPlayer().getCollision().getCurrent();
        CubeRegion region = App.getCubeManager().getRegion(cube.getRegion());

        for (Cube[] temp : region.getCubes()) {
            for (Cube c : temp) {
                if (c != null) {
                    if (c.getX() >= point.x - radius && c.getX() <= point.x + radius) {
                        if (c.getY() >= point.y - radius && c.getY() <= point.y + radius) {
                            if (cube != c && c.isVisible()) {
                                if (c.blocksRay(c, from, to)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
