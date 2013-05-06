package com.kingx.dungeons;

public enum Block {

    DIRT(5, "dirt"),
    GRASS(5, "dirt_side", "dirt_top", "dirt"),
    SAND(5, "sand"),
    RED(15, "red"),
    GRAVEL(45, "gravel"),
    ROCK(135, "rock"),
    BEDROCK(405, "bedrock"),

    OBSIDIAN(10, "obsidian"),
    MOONSTONE(30, "moonstone"),
    EMERALD(90, "emerald"),
    RUBY(270, "ruby"),
    DIAMOND(810, "diamond"),

    STONE(0, "stone"),
    STONE_PAVEMENT(0, "stone_side", "stone_top", "stone");
    /*
        

        */

    private final int hardness;
    private final String[] textures;

    Block(int hardness, String[] textures) {
        this.hardness = hardness;
        this.textures = textures;
    }

    Block(int hardness, String texture) {
        this.hardness = hardness;
        this.textures = new String[6];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = texture;
        }
    }

    Block(int hardness, String sideTexture, String topTexture, String bottomTexture) {
        this.hardness = hardness;
        this.textures = new String[6];
        this.textures[0] = sideTexture;
        this.textures[1] = sideTexture;
        this.textures[2] = sideTexture;
        this.textures[3] = sideTexture;
        this.textures[4] = topTexture;
        this.textures[5] = bottomTexture;
    }

    public String getTextureName(int i) {
        return textures[i];
    }

    public int getHardness() {
        return hardness;
    }

}
