package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;

import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;
import com.kingx.dungeons.graphics.Terrain;

public abstract class CubeSideFactory {

    private Cube[][] cubes;
    private final ArrayList<CubeRegion> regions;

    public CubeSideFactory(Terrain maze, float size, boolean computeBoundaries) {

        regions = new ArrayList<CubeRegion>();
        for (int i = 0; i < maze.getFootprints(); i++) {
            cubes = new Cube[maze.getFootprint(i).length + 1][maze.getFootprint(i)[0].length];
            for (int j = 0; j < cubes.length - 1; j++) {
                for (int k = 0; k < cubes[j].length; k++) {

                    float x = 0, y = 0, z = 0;

                    switch (i) {
                        case 0:
                            x = j;
                            y = k;
                            z = 0;
                            break;
                        case 1:
                            x = maze.getFootprint(i).length;
                            y = k;
                            z = -j;
                            break;
                        case 2:
                            x = maze.getFootprint(i).length - j;
                            y = k;
                            z = -maze.getFootprint(i).length;
                            break;
                        case 3:
                            x = 0;
                            y = k;
                            z = -maze.getFootprint(i).length + j;
                            break;
                    }
                    cubes[j][k] = CubeFactory.makeCube(i, x * App.UNIT, y * App.UNIT, z * App.UNIT, size, getBlock(maze.getFootprint(i)[j][k]), j, k);
                    if (j == 0) {
                        cubes[j][k].setCorner(true);
                    }
                    cubes[j][k].setVisible(i, maze.getFootprint(i)[j][k] != null);

                }
            }

            regions.add(new CubeRegion(i, cubes, computeBoundaries));

        }

        for (int i = 0; i < regions.size(); i++) {
            Cube[][] current = regions.get(i).getCubes();
            Cube[][] next = regions.get((i + 1) % regions.size()).getCubes();

            int last = current.length - 1;

            for (int k = 0; k < current[last].length; k++) {
                current[last][k] = next[0][k];
                current[last][k].setCorner(true);
            }
        }
    }

    protected abstract Block getBlock(BlockPair blockPair);

    public ArrayList<CubeRegion> getCubeRegions() {
        return regions;
    }

}