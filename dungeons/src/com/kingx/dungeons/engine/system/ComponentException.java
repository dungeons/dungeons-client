package com.kingx.dungeons.engine.system;

public class ComponentException extends Exception {

    public ComponentException() {
        super("Required component not found.");
    }

}
