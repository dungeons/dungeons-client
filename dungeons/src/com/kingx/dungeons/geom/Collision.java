package com.kingx.dungeons.geom;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.geom.Point.Int;
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
        if ((triangles.length / 3) % 3 != 0)
            throw new RuntimeException("triangle list size is not a multiple of 3");

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
            if (t < 0)
                return false;

            if (intersection != null)
                intersection.set(ray.origin).add(tmp(ray.direction).mul(t));
            return true;
        } else if (plane.testPoint(ray.origin) == Plane.PlaneSide.OnPlane) {
            if (intersection != null)
                intersection.set(ray.origin);
            return true;
        } else
            return false;
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
        if (!intersectRayPlane(ray, p, i))
            return false;

        v0.set(t3).sub(t1);
        v1.set(t2).sub(t1);
        v2.set(i).sub(t1);

        float dot00 = v0.dot(v0);
        float dot01 = v0.dot(v1);
        float dot02 = v0.dot(v2);
        float dot11 = v1.dot(v1);
        float dot12 = v1.dot(v2);

        float denom = dot00 * dot11 - dot01 * dot01;
        if (denom == 0)
            return false;

        float u = (dot11 * dot02 - dot01 * dot12) / denom;
        float v = (dot00 * dot12 - dot01 * dot02) / denom;

        if (u >= 0 && v >= 0 && u + v <= 1) {
            if (intersection != null)
                intersection.set(i);
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
    private static boolean inBetween(Vector3 a, Vector3 b, Vector3 c) {
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

    public static void correct(PositionComponent position, SizeComponent size) {
        float z, x;
        float offset = 1 - App.MAP_OFFSET;

        x = position.getX();
        x = Math.max(x, CubeRegion.min.x + offset);
        x = Math.min(x, CubeRegion.max.x - offset);

        z = position.getZ();
        z = Math.min(z, CubeRegion.max.z - offset);
        z = Math.max(z, CubeRegion.min.z + offset);

        position.setX(x);
        position.setZ(z);
    }

    /**
     * Check whether is point in map bounds and walkable
     * 
     * @param point
     *            to be checked
     * @return {@code true} if point is walkable, {@code false} otherwise
     */
    public static boolean isWalkable(Int point) {
        boolean[][] footprint = App.getMap().getFootprint();

        if (point.x == footprint.length) {
            footprint = App.getMap().getNextFootprint();
            point.x = 0;
        }

        if (point.x < 0 || point.x >= footprint.length) {
            return true;
        }

        if (point.y < 0 || point.y >= footprint[0].length) {
            return false;
        }

        return footprint[point.x][point.y];
    }
}
