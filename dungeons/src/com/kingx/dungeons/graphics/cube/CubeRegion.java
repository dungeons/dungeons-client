package com.kingx.dungeons.graphics.cube;

import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point.Int;

public class CubeRegion {

    private Cube[][] cubes;

    public CubeRegion(Cube[][] cubes) {
        setCubes(cubes);
    }

    public Cube[][] getCubes() {
        return cubes;
    }

    public void setCubes(Cube[][] cubes) {
        this.cubes = cubes;
        computeBoundaries(cubes);
    }

    public static Vector3 min = new Vector3();
    public static Vector3 mean = new Vector3();
    public static Vector3 max = new Vector3();

    static {
        resetBoundaries();
    }

    private void computeBoundaries(Cube[][] cubes) {
        for (Cube[] temp : cubes) {
            for (Cube cube : temp) {
                if (cube != null) {
                    for (CubeVertex vert : cube.getVerts()) {

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
            }
        }

        mean.set((max.x + min.x) / 2f, (max.y + min.y) / 2f, (max.z + min.z) / 2f);

        System.out.println("Min:  " + min);
        System.out.println("Max:  " + max);
        System.out.println("Mean: " + mean);
    }

    private static void resetBoundaries() {
        min.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        max.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
    }

    public static int getWidth() {
        return (int) ((max.x - min.x) / App.UNIT);
    }

    public void removeCube(Int point) {
        cubes[point.x][point.y] = null;
    }

    public void removeCube(int x, int y) {
        cubes[x][y] = null;
    }

    @Override
    public String toString() {
        String output = "";
        int cnt = 0;
        for (Cube[] temp : cubes) {
            for (Cube cube : temp) {
                if (cube != null) {
                    output += "X";
                    cnt++;
                } else {
                    output += "O";
                }
            }
            output += "\t";

            for (Cube cube : temp) {
                output += String.format("%1$12s", cube != null ? cube.hashCode() : "[null]");
            }
            output += "\n";
        }

        output += "\n total [" + cnt + "]";
        return output;
    }

}
