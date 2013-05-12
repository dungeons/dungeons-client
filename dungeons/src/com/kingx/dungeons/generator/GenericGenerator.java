package com.kingx.dungeons.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kingx.dungeons.App;
import com.kingx.dungeons.Block;
import com.kingx.dungeons.BlockPair;

public class GenericGenerator extends AbstractGenerator {
    private BlockPair[][] terain;

    List<Trigger> chances = new ArrayList<Trigger>();

    @Override
    public BlockPair[][] build(int width, int height) {
        terain = new BlockPair[width][height];

        Block[] zones = new Block[] { Block.BEDROCK, Block.ROCK, Block.GRAVEL, Block.RED, Block.SAND, Block.DIRT };
        int zoneSize = height / zones.length;
        int scatterZoneSize = zoneSize / 4;

        for (int i = 1; i < zones.length; i++) {
            int min = i * zoneSize - 1;
            int max = i * zoneSize + scatterZoneSize;
            TerainMutator b1 = new BlockMutator(terain, zones[i - 1], min, max);
            chances.add(new Trigger(2, new SpotMutator(b1)));
        }

        for (int i = 0; i < terain.length; i++) {
            for (int j = 0; j < terain[i].length; j++) {
                Block block = zones[Math.min(j / zoneSize, zones.length - 1)];
                terain[i][j] = new BlockPair(block, null, null, false);
            }
        }

        zones = new Block[] { Block.DIAMOND, Block.EMERALD, Block.RUBY, Block.MOONSTONE, Block.OBSIDIAN };
        zoneSize = height / zones.length;
        scatterZoneSize = zoneSize / 4;

        for (int i = 0; i < zones.length; i++) {
            int min = i * zoneSize;
            int max = (i + 1) * zoneSize;
            TerainMutator b1 = new MineralMutator(terain, zones[i], min, max);
            chances.add(new Trigger(20, new SpotMutator(b1)));
        }

        /*  CaveMutator c = new CaveMutator(terain, 5, 5, 0, 45);

          chances.add(new Trigger(100, c));*/

        GlacierCaveMutator ca = new GlacierCaveMutator(terain, 4, 4);

        chances.add(new Trigger(100, ca));

        /*   int gems = 5;
           int zone = height / gems * 2;
           for (int i = 0; i < gems; i++) {

               int min = height - (int) (i * (zone / 2f) + zone);
               int max = height - (int) (i * (zone / 2f));
               System.out.format("%d %d %d\n", min, max, i);
               TerainMutator b1 = new BlockMutator(terain, Block.values()[Block.OBSIDIAN.ordinal() + i], min, max);
               TerainMutator b2 = new BlockMutator(terain, Block.values()[Block.SAND.ordinal() + i], min, max);
               chances.add(new Trigger(12, new SpotMutator(b1)));
               chances.add(new Trigger(4, new SpotMutator(b2)));

           }*/
        //chances.add(new Trigger(10, new SpotMutator(terain, null, 10, 40)));
        //  chances.add(new Trigger(10, new SpotMutator(terain, 100 + 1, 0, 50)));
        //chances.add(new Trigger(100, new MineralCapsle(terain, 0, 1)));
        Collections.sort(chances);

        for (int i = 0; i < terain.length; i++) {
            for (int j = 0; j < terain[i].length; j++) {
                for (Trigger chance : chances) {
                    chance.initMutator(i, j);
                }
            }
        }

        for (int i = 0; i < terain.length; i++) {
            terain[i][terain[i].length - 1] = new BlockPair(Block.GRASS, null, null, false);
        }

        /*

        terain[2][terain[0].length - 1] = Block.STONE;
        terain[3][terain[0].length - 1] = Block.STONE;
        terain[5][terain[0].length - 1] = Block.STONE;
        terain[6][terain[0].length - 1] = Block.STONE;
        */
        terain[0][terain[0].length - 1].setRemoved(true);

        return terain;
    }

    private static class Trigger implements Comparable<Trigger> {
        private final int chance;
        private final Mutable mutator;

        public Trigger(int chance, Mutable mutator) {
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
