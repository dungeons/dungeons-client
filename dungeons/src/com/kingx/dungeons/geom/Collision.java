package com.kingx.dungeons.geom;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class Collision {

    static Vector3 tmp = new Vector3();
    static Vector3 best = new Vector3();
    static Vector3 tmp1 = new Vector3();
    static Vector3 tmp2 = new Vector3();
    static Vector3 tmp3 = new Vector3();

    /**
     * Intersects the given ray with list of triangles. Returns the first found intersection point in intersection
     * 
     * @param ray
     *            The ray
     * @param triangles
     *            The triangles, each successive 3 elements from a vertex
     * @param intersection
     *            The nearest intersection point (optional)
     * @return Whether the ray and the triangles intersect.
     */
    public static boolean intersectRayTrianglesBetweenPoints(Ray ray, float[] triangles, Vector3 a, Vector3 b) {
        if ((triangles.length / 3) % 3 != 0)
            throw new RuntimeException("triangle list size is not a multiple of 3");

        for (int i = 0; i < triangles.length - 6; i += 9) {
            boolean result = Intersector.intersectRayTriangle(ray, tmp1.set(triangles[i], triangles[i + 1], triangles[i + 2]),
                    tmp2.set(triangles[i + 3], triangles[i + 4], triangles[i + 5]), tmp3.set(triangles[i + 6], triangles[i + 7], triangles[i + 8]), tmp);

            if (result) {
                if (inBetween(a, b, tmp)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean inBetween(Vector3 a, Vector3 b, Vector3 c) {
        float minx = Math.min(a.x, b.x);
        float miny = Math.min(a.y, b.y);
        float maxx = Math.max(a.x, b.x);
        float maxy = Math.max(a.y, b.y);
        return c.x >= minx && c.x <= maxx && c.y >= miny && c.y <= maxy;
    }

}
