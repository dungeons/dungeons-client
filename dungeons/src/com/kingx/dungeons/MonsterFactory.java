package com.kingx.dungeons;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.concrete.Zombie;
import com.kingx.dungeons.graphics.Terrain;
import com.kingx.dungeons.graphics.cube.Cube;
import com.kingx.dungeons.graphics.cube.CubeRegion;

public class MonsterFactory {

    private final World world;

    public MonsterFactory(World world, Terrain maze, ArrayList<CubeRegion> cubeDefaultSides) {

        this.world = world;
        for (int i = 0; i < maze.getFootprints(); i++) {
            for (int j = 0; j < maze.getFootprint(i).length; j++) {
                for (int k = 0; k < maze.getFootprint(i)[j].length; k++) {

                    createMonster(maze.getFootprint(i)[j][k].getSpawn(), cubeDefaultSides.get(i).getCubes()[j][k]);
                }
            }
        }
    }

    private void createMonster(Monster monster, Cube cube) {
        if (monster != null) {
            switch (monster) {
                case ZOMBIE:
                    createZombie(cube.mean);
                    break;
                default:
                    break;
            }
        }
    }

    public void createZombie(Vector3 position) {
        System.out.println("Creating om " + position);
        Zombie zombie = new Zombie(world, position, 1f, 1f);
        zombie.createEntity().addToWorld();
    }

}