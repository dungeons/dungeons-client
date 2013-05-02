package com.kingx.dungeons.graphics.cube;

import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;

public final class CubeTopFactory {

    private final Cube[][] cubes;
    private final CubeRegion region;

    public CubeTopFactory(int size, int x, int y, int z) {

        cubes = new Cube[size][size];
        for (int i = 0; i < cubes.length; i++) {
            for (int j = 0; j < cubes[i].length; j++) {

                cubes[i][j] = CubeFactory.makeCube(0, (x + i) * App.UNIT, y * App.UNIT, (z - j) * App.UNIT, App.UNIT, Block.GRASS, 0, 0);
                cubes[i][j].setVisible(0, false);
                cubes[i][j].setVisibleSide(0, CubeSideType.BACK, false);
                cubes[i][j].setVisibleSide(0, CubeSideType.TOP, true);

            }
        }

        region = new CubeRegion(0, cubes);

    }

    public CubeRegion getCubeRegions() {
        return region;
    }

}