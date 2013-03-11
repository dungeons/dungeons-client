package com.kingx.dungeons.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Texture is divided into four regions.
 * 
 * @see FrameBuffer
 * @author xkings
 */
public class QuadTextureFrameBuffer extends FrameBuffer {

    /** current texture index */
    private int currentTexture = -1;
    private final int maxTextures = 4;

    public void nextTexture() {
        currentTexture = (currentTexture + 1) % maxTextures;
        int x = (currentTexture % 2) * colorTexture.getWidth() / 2;
        int y = (currentTexture / 2) * colorTexture.getHeight() / 2;
        Gdx.graphics.getGL20().glViewport(x, y, colorTexture.getWidth() / 2, colorTexture.getHeight() / 2);

    }

    public int getCurrentTexture() {
        return currentTexture;
    }

    public void setCurrentTexture(int currentTexture) {
        this.currentTexture = currentTexture;
    }

    public QuadTextureFrameBuffer(Format format, int width, int height, boolean hasDepth) {
        super(format, width, height, hasDepth);
    }

}
