package com.kingx.dungeons.server;

public class ServerCommand {

    private final int id;
    private final short component;
    private final short componentProperty;
    private final int value;

    public ServerCommand(int id, short component, short componentProperty, int value) {
        this.id = id;
        this.component = component;
        this.componentProperty = componentProperty;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public short getComponent() {
        return component;
    }

    public short getComponentProperty() {
        return componentProperty;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ServerCommand [id=" + id + ", component=" + component + ", componentProperty=" + componentProperty + ", value=" + value + "]";
    }

}
