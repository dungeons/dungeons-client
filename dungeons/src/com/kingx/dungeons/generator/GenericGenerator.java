package com.kingx.dungeons.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kingx.dungeons.App;

public class GenericGenerator extends AbstractGenerator {
    private int[][] terain;

    List<Trigger> chances = new ArrayList<Trigger>();

    @Override
    public int[][] build(int width, int height) {
        terain = new int[width][height];

        int gems = 5;
        int zone = height / gems;
        for (int i = 0; i < gems; i++) {

            //  chances.add(new Trigger(10, new SpotMutator(terain, 6 - i, i * zone, i * (zone + 1))));
        }
        //chances.add(new Trigger(100, new MineralCapsle(terain, 0, 1)));
        Collections.sort(chances);

        for (int i = 0; i < terain.length; i++) {
            for (int j = 0; j < terain[i].length; j++) {
                terain[i][j] = 1;
            }
        }

        for (int i = 0; i < terain.length; i++) {
            for (int j = 0; j < terain[i].length; j++) {
                for (Trigger chance : chances) {
                    chance.initMutator(i, j);
                }
            }
        }

        for (int i = 0; i < terain.length; i++) {
            terain[i][terain[i].length - 1] = 2;
        }
        terain[0][terain[0].length - 1] = 0;

        return terain;
    }

    private static class Trigger implements Comparable<Trigger> {
        private final int chance;
        private final TerainMutator mutator;

        public Trigger(int chance, TerainMutator mutator) {
            this.chance = chance;
            this.mutator = mutator;
        }

        public void initMutator(int x, int y) {
            if (!isValid())
                return;

            mutator.mutate(x, y);
        }

        private boolean isValid() {
            return App.rand.nextInt(chance) == 0;
        }

        @Override
        public int compareTo(Trigger o) {
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
