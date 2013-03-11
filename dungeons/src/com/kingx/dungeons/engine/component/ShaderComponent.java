package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderComponent extends Component {
    public ShaderProgram shader;
    public Color color;
    public TextureRegion texture;

    public ShaderComponent(ShaderProgram shader, Color color, TextureRegion texture) {
        this.shader = shader;
        this.color = color;
        this.texture = texture;
    }

}
