package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.kingx.dungeons.App;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.geom.Point.Int;
import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;

public class CubeManager {
    public ArrayList<ArrayList<CubeRegion>> cubeRegionPacks;
    private final CubeRegion cubeTop;

    public ArrayList<CubeRegion> getBlockSides() {
        return cubeRegionPacks.get(0);
    }

    public ArrayList<CubeRegion> getMineralSides() {
        return cubeRegionPacks.get(1);
    }

    public CubeRegion getTop() {
        return cubeTop;
    }

    public CubeManager(CubeRegion cubeTop, ArrayList<CubeRegion> blocks, ArrayList<CubeRegion> minerals) {
        this.cubeRegionPacks = new ArrayList<ArrayList<CubeRegion>>();
        cubeRegionPacks.add(blocks);
        cubeRegionPacks.add(minerals);
        this.cubeTop = cubeTop;
        hideInvidibleParts();
    }

    private void hideInvidibleParts() {
        Cube[][] cubes;
        for (int i = 0; i < cubeRegionPacks.size(); i++) {
            ArrayList<CubeRegion> pack = cubeRegionPacks.get(i);
            for (int j = 0; j < pack.size(); j++) {
                CubeRegion region = pack.get(j);
                cubes = region.getCubes();
                for (int k = 0; k < cubes.length; k++) {
                    for (int l = 0; l < cubes[k].length; l++) {
                        if (App.getTerrain().getFootprint(j, k, l) != null) {
                            checkAdjacentBlocks(region, k, l);
                        }
                    }
                }
            }
        }
    }

    private void checkAdjacentBlocks(CubeRegion region, int x, int y) {
        Cube cube = getCubeAt(region, x, y);
        if (cube != null && cube.isVisible()) {
            Cube up = getCubeAt(region, x, y + 1);
            Cube down = getCubeAt(region, x, y - 1);
            Cube left = getCubeAt(region, x - 1, y);
            Cube right = getCubeAt(region, x + 1, y);

            if (!cube.corner) {
                cube.setVisibleSide(region.getId(), CubeSideType.BACK, false);

                cube.setVisibleSide(region.getId(), CubeSideType.TOP, up == null || !up.isVisible());
                cube.setVisibleSide(region.getId(), CubeSideType.BOTTOM, down == null || !down.isVisible());
                cube.setVisibleSide(region.getId(), CubeSideType.LEFT, left == null || !left.isVisible());
                cube.setVisibleSide(region.getId(), CubeSideType.RIGHT, right == null || !right.isVisible());

                cube.setVisibleSide(region.getId(), CubeSideType.FRONT, true);
            }
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
        removeCubeInternal(point, this.getBlockSides());
        removeCubeInternal(point, this.getMineralSides());
    }

    public void removeCubeInternal(Int point, ArrayList<CubeRegion> regionPack) {
        BlockPair[][] footprint = App.getTerrain().getFootprint();

        if (point.x == 0) {
            removeCube(regionPack.get(App.getPrevView()), footprint.length, point.y);
        } else if (point.x == footprint.length) {
            removeCube(regionPack.get(App.getNextView()), 0, point.y);
        }
        removeCube(regionPack.get(App.getCurrentView()), point.x, point.y);

        App.getTerrain().setFootprint(App.getCurrentView(), point.x, point.y, null);

    }

    public ArrayList<Cube> getCube(Int point) {
        ArrayList<Cube> cubes = new ArrayList<Cube>();
        cubes.add(getCubeInternal(point, this.getBlockSides()));
        cubes.add(getCubeInternal(point, this.getMineralSides()));
        return cubes;

    }

    public Cube getCubeInternal(Int point, ArrayList<CubeRegion> regionPack) {
        BlockPair[][] footprint = App.getTerrain().getFootprint();

        if (point.x == 0) {
            return regionPack.get(App.getPrevView()).getCubes()[footprint.length][point.y];
        } else if (point.x == footprint.length) {
            return regionPack.get(App.getNextView()).getCubes()[0][point.y];
        }

        return regionPack.get(App.getCurrentView()).getCubes()[point.x][point.y];

    }

    private void removeCube(CubeRegion region, int x, int y) {
        region.removeCube(x, y);
        checkCubeRegion(region, x, y);
    }

    public void checkCubeRegion(int region, int x, int y) {
        checkCubeRegion(this.getBlockSides().get(region), x, y);
    }

    public void checkCubeRegion(CubeRegion region, int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                checkAdjacentBlocks(region, i, j);
            }
        }
    }
}
