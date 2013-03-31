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

        boolean[][] footprint = App.getMap().getFootprint(point);

        footprint[point.x][point.y] = true;

    }
}
