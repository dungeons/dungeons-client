package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.kingx.dungeons.App;

public class CubeSkyFactory {

    private Cube[][] cubes;
    private final ArrayList<CubeRegion> regions;

    public CubeSkyFactory(float size) {
        regions = new ArrayList<CubeRegion>();

        int count = 10;
        for (int i = 0; i < count; i++) {
            cubes = new Cube[count][count];
            for (int j = 0; j < cubes.length; j++) {
                for (int k = 0; k < cubes[j].length; k++) {
                    cubes[j][k] = CubeFactory.makeCube(0, i * App.UNIT, j * App.UNIT, k * App.UNIT, size, null, j, k);
                }
            }
        }

        regions.add(new CubeRegion(0, cubes, false));
    }

    public ArrayList<CubeRegion> getCubeRegions() {
        return regions;
    }

}