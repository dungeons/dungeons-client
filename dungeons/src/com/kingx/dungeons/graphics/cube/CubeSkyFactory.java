package com.kingx.dungeons.graphics.cube;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class CubeSkyFactory {

    private final List<SimpleCube> cubes = new ArrayList<SimpleCube>();

    public CubeSkyFactory(float x, float y, float z, float size, int count, float offset) {
        /*  for (int i = 0; i < count; i++) {
              for (int j = 0; j < count; j++) {
                  for (int k = 0; k < count; k++) {
                      cubes.add(CubeFactory.makeCube(x + i * offset, y + j * offset, z + k * offset, size, Color.WHITE));
                  }
              }
          }*/

        for (int i = 0; i < count * 2; i++) {
            float height = offset / count;
            float ang = (float) (Math.PI * 2) / count;
            for (int j = 0; j < count; j++) {
                float skew = ang / 2 * i;
                float offx = (float) (Math.cos(ang * j + skew) * offset);
                float offz = (float) (Math.sin(ang * j + skew) * offset);
                cubes.add(CubeFactory.makeCube(x + offx, y + height * i, z + offz, size, Color.WHITE));
            }
        }
    }

    public List<SimpleCube> getCubes() {
        return cubes;
    }

}