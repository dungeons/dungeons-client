package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

public class CubeRegion {

    private ArrayList<Cube> cubes;

    public ArrayList<Cube> getCubes() {
        return cubes;
    }

    public void setCubes(ArrayList<Cube> cubes) {
        this.cubes = cubes;
    }

    public void addCubes(ArrayList<Cube> cubes) {
        this.cubes.addAll(cubes);
    }

}
