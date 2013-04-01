package com.kingx.dungeons.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kingx.dungeons.App;

public class GenericGenerator extends AbstractGenerator {
    private int[][] maze;

    List<Chance> chances = new ArrayList<Chance>();

    @Override
    public int[][] build(int width, int height) {
        maze = new int[width][height];

        chances.add(new Chance(10, 0));
        chances.add(new Chance(50, 2));
        Collections.sort(chances);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = 1;
            }
        }

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                for (Chance chance : chances) {
                    if (chance.isValid()) {
                        maze[i][j] = chance.getValue();
                    }
                }
            }
        }

        return maze;
    }

    private static class Chance implements Comparable<Chance> {
        private final int chance;
        private final int value;

        public Chance(int chance, int value) {
            this.chance = chance;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public boolean isValid() {
            return App.rand.nextInt(chance) == 0;
        }

        @Override
        public int compareTo(Chance o) {
            if (this.chance > o.chance) {
                return 1;
            } else if (this.chance < o.chance) {
                return -1;
            } else {
                return 0;
            }
        }

    }

}
