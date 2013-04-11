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
                        checkAdjacentBlocks(region, j, k);

                    }
                }
            }
        }
    }

    private void checkAdjacentBlocks(CubeRegion region, int x, int y) {
        Cube[][] cubes = region.getCubes();
        Cube up = getCubeAt(region, x, y + 1);
        Cube down = getCubeAt(region, x, y - 1);
        Cube left = getCubeAt(region, x - 1, y);
        Cube right = getCubeAt(region, x + 1, y);

        if (up != null) {
            cubes[x][y].hide(2);
        }

        if (down != null) {
            cubes[x][y].hide(0);
        }

        if (left != null) {
            cubes[x][y].hide(3);
        }

        if (right != null) {
            cubes[x][y].hide(1);
        }
    }

    private Cube getCubeAt(CubeRegion region, int x, int y) {
        if (x < 0 || x >= region.getCubes().length) {
            return null;
        }
        if (y < 0 || y >= region.getCubes()[x].length) {
            return null;
        }
        return region.getCubes()[x][y];
    }

    public void removeCube(Int point) {
        int[][] footprint = App.getMap().getFootprint();

        if (point.x == 0) {
            removeCube(this.getCubeRegions().get(App.getPrevView()), footprint.length, point.y);
        } else if (point.x == footprint.length) {
            removeCube(this.getCubeRegions().get(App.getNextView()), 0, point.y);
        }
        removeCube(this.getCubeRegions().get(App.getCurrentView()), point.x, point.y);

        if (point.x == footprint.length) {
            footprint = App.getMap().getNextFootprint();
            footprint[0][point.y] = 0;
        } else {
            footprint[point.x][point.y] = 0;
        }
    }

    private void removeCube(CubeRegion region, int x, int y) {
        region.removeCube(x, y);
        // TODO finish this
        //checkAdjacentBlocks(region, x, y);
    }
}
