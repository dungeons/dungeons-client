package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kingx.artemis.Component;

public class ShaderComponent extends Component {
    private ShaderProgram shader;

    public ShaderComponent(ShaderProgram shader) {
        this.shader = shader;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public void setShader(ShaderProgram shader) {
        this.shader = shader;
    }

}
