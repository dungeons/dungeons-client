package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.kingx.dungeons.App;
import com.kingx.dungeons.geom.Point.Int;

public class CubeManager {
    public ArrayList<CubeRegion> cubeRegions;

    public ArrayList<CubeRegion> getCubeRegions() {
        return cubeRegions;
    }

    public CubeManager(ArrayList<CubeRegion> cubeRegions) {
        this.cubeRegions = cubeRegions;
    }

    public void removeCube(Int point) {

        int current = App.getCurrentView();

        CubeRegion region = cubeRegions.get(current);
        region.removeCube(point);

        int[][] footprint = App.getMap().getFootprint();
        point = point.cpy();
        if (point.x == footprint.length) {
            footprint = App.getMap().getNextFootprint();
            point.x = 0;
        }
        footprint[point.x][point.y] = 0;

    }
}
