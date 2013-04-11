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
        hideInvidibleParts();
    }

    private void hideInvidibleParts() {
        Cube[][] cubes;
        for (int i = 0; i < cubeRegions.size(); i++) {
            CubeRegion region = cubeRegions.get(i);
            cubes = region.getCubes();
            for (int j = 0; j < cubes.length; j++) {
                for (int k = 0; k < cubes[j].length; k++) {
                    if (cubes[j][k] != null) {
                        Cube up = getCubeAt(region, j, k + 1);
                        Cube down = getCubeAt(region, j, k - 1);
                        Cube left = getCubeAt(region, j - 1, k);
                        Cube right = getCubeAt(region, j + 1, k);

                        if (up != null) {
                            cubes[j][k].hide(2);
                        }

                        if (down != null) {
                            cubes[j][k].hide(0);
                        }

                        if (left != null) {
                            cubes[j][k].hide(3);
                        }

                        if (right != null) {
                            cubes[j][k].hide(1);
                        }
                    }
                }
            }
        }
    }

    private Cube getCubeAt(CubeRegion region, int j, int k) {
        if (j < 0 || j >= region.getCubes().length) {
            return null;
        }

        if (k < 0 || k >= region.getCubes()[j].length) {
            return null;
        }
        return region.getCubes()[j][k];
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
