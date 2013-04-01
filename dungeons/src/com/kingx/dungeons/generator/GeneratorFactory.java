package com.kingx.dungeons.generator;

public class GeneratorFactory {

    public static AbstractGenerator getInstace(GeneratorType generator) {
        switch (generator) {
            case GENERIC:
                return new GenericGenerator();
            case MAZE:
                return new MazeGenerator();
            default:
                throw new IllegalArgumentException("[" + generator + "] is not valid generator type.");
        }
    }
}
