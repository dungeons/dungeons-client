package com.kingx.dungeons.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderException extends RuntimeException {

    public ShaderException(String name, ShaderProgram shader) {
        super("Compilation of shader [" + name + "] was unsuccessful.\n" + shader.getLog());
    }
}
