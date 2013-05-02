package com.kingx.dungeons;

public enum Mineral {

    MINERAL("mineral");

    private final String[] textures;

    Mineral(String[] textures) {
        this.textures = textures;
    }

    Mineral(String texture) {
        this.textures = new String[6];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = texture;
        }
    }

    Mineral(String sideTexture, String topTexture, String bottomTexture) {
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

}
