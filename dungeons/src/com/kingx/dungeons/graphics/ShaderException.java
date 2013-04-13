package com.kingx.dungeons.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -4694794243512334997L;

    public ShaderException(String name, ShaderProgram shader) {
        super("Compilation of shader [" + name + "] was unsuccessful.\n" + shader.getLog() + "|");
    }
}
