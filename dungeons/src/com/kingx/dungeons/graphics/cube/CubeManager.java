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

        int[][] footprint = App.getMap().getFootprint();

        if (point.x == 0) {
            this.getCubeRegions().get(App.getPrevView()).removeCube(footprint.length, point.y);
        } else if (point.x == footprint.length) {
            this.getCubeRegions().get(App.getNextView()).removeCube(0, point.y);
        }
        this.getCubeRegions().get(App.getCurrentView()).removeCube(point.x, point.y);

        if (point.x == footprint.length) {
            footprint = App.getMap().getNextFootprint();
            footprint[0][point.y] = 0;
        } else {
            footprint[point.x][point.y] = 0;

        }
    }
}
