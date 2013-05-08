package com.kingx.dungeons;

public enum Block {

    DIRT(5, BlockTextures.DIRT, BlockTextures.UNKNOWN),
    GRASS(5, BlockTextures.GRASS, BlockTextures.UNKNOWN),
    SAND(5, BlockTextures.SAND, BlockTextures.UNKNOWN),
    RED(15, BlockTextures.RED, BlockTextures.UNKNOWN),
    GRAVEL(45, BlockTextures.GRAVEL, BlockTextures.UNKNOWN),
    ROCK(135, BlockTextures.ROCK, BlockTextures.UNKNOWN),
    BEDROCK(405, BlockTextures.BEDROCK, BlockTextures.UNKNOWN),

    OBSIDIAN(10, BlockTextures.OBSIDIAN, BlockTextures.UNKNOWN),
    MOONSTONE(30, BlockTextures.MOONSTONE, BlockTextures.UNKNOWN),
    EMERALD(90, BlockTextures.EMERALD, BlockTextures.UNKNOWN),
    RUBY(270, BlockTextures.RUBY, BlockTextures.UNKNOWN),
    DIAMOND(810, BlockTextures.DIAMOND, BlockTextures.UNKNOWN),

    STONE(0, BlockTextures.STONE, BlockTextures.UNKNOWN),
    STONE_PAVEMENT(0, BlockTextures.STONE_PAVEMENT, BlockTextures.UNKNOWN),

    ICE(0, BlockTextures.ICE, BlockTextures.UNKNOWN),
    ICE_PATH(0, BlockTextures.ICE_PATH, BlockTextures.UNKNOWN);

    public static final Block DEFAULT = DIRT;
    private final int hardness;
    private final String[] textures;
    private final String[] defaultTextures;

    Block(int hardness, String[] textures, String[] defaultTextures) {
        this.hardness = hardness;
        this.textures = textures;
        this.defaultTextures = defaultTextures;
    }

    public String getTextureName(int i) {
        return textures[i];
    }

    public String getDefaultTextureName(int i) {
        return defaultTextures[i];
    }

    public int getHardness() {
        return hardness;
    }

}
