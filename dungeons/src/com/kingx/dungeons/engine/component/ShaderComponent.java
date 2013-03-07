package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderComponent extends Component {
    public ShaderProgram shader;
    public Color color;

    public ShaderComponent(ShaderProgram shader, Color color) {
        this.shader = shader;
        this.color = color;
    }

    @Override
    public String toString() {
        return "ShaderComponent [shader=" + shader + ", color=" + color + "]";
    }

}
