package com.kingx.dungeons.entity;

public interface EntityListener {

    public void positionChange(float x, float y, float z);

    public void rotationChange(float rotation);
}
