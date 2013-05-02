package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kingx.artemis.Component;

public class TextureComponent extends Component {
    private static final float SCALE = 1 / 16f;
    private Color tint;
    private float rotation;
    private final float width;
    private final float height;
    private TextureRegion texture;

    public TextureComponent(TextureRegion texture) {
        this(texture, 0);
    }

    public TextureComponent(TextureRegion texture, float rotation) {
        this(texture, new Color(Color.WHITE), rotation);
    }

    public TextureComponent(TextureRegion texture, Color tint, float rotation) {
        this.texture = texture;
        this.tint = tint;
        this.rotation = rotation;

        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
    }

    public Color getTint() {
        return tint;
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getWidth() {
        return width * SCALE;
    }

    public float getHeight() {
        return height * SCALE;
    }

    public float getWidthInPixels() {
        return width;
    }

    public float getHeightInPixels() {
        return height;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public TextureRegion getTexture() {
        return texture;
    }

}
