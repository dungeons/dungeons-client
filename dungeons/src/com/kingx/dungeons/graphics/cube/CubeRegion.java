package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class CubeRegion {

    private ArrayList<Cube> cubes;

    public CubeRegion() {
        resetBoundaries();
    }

    public ArrayList<Cube> getCubes() {
        return cubes;
    }

    public void setCubes(ArrayList<Cube> cubes) {
        this.cubes = cubes;
        resetBoundaries();
        computeBoundaries(cubes);
    }

    public void addCubes(ArrayList<Cube> cubes) {
        this.cubes.addAll(cubes);
        computeBoundaries(cubes);
    }

    public static Vector3 min = new Vector3();
    public static Vector3 mean = new Vector3();
    public static Vector3 max = new Vector3();

    private void computeBoundaries(ArrayList<Cube> cubes) {
        for (Cube cube : cubes) {
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

        mean.set((max.x + min.x) / 2f, (max.y + min.y) / 2f, (max.z + min.z) / 2f);

        System.out.println("Min:  " + min);
        System.out.println("Max:  " + max);
        System.out.println("Mean: " + mean);
    }

    private void resetBoundaries() {
        min.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        max.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
    }

    public static float getWidth() {
        return max.x - min.x;
    }

}
