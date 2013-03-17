package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderComponent extends Component {
    private ShaderProgram shader;
    private Color color;
    private TextureRegion texture;

    public ShaderComponent(ShaderProgram shader, Color color, TextureRegion texture) {
        this.shader = shader;
        this.color = color;
        this.texture = texture;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public void setShader(ShaderProgram shader) {
        this.shader = shader;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

}
