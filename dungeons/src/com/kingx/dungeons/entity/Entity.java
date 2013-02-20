package com.kingx.dungeons.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public abstract class Entity {
    protected Vector3 position;
    protected float rotation;
    protected float size;
    protected float speed;

    private boolean active = false;

    public Entity(float x, float y, float z, float size, float speed) {
        this.position = new Vector3(x, y, z);
        this.size = size;
        this.speed = speed;
    }

    public void update(float delta) {
        if (active) {
            doUpdate(delta);
        }
    }

    /**
     * Method overidable by subclasses, do not call directly
     */
    protected abstract void doUpdate(float delta);

    // Listners
    // TODO these listener eat up a lot of sapce look into java swing listeners and find a better solution

    private ArrayList<EntityListener> listeners = new ArrayList<EntityListener>();

    public void registerListener(EntityListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(EntityListener listener) {
        listeners.remove(listener);
    }

    private void updatePositionListeners() {
        for (EntityListener el : listeners) {
            el.positionChange(this.position.x, this.position.y, this.position.z);
        }
    }

    private void updateRotationListeners() {
        for (EntityListener el : listeners) {
            el.rotationChange(this.rotation);
        }
    }

    // ///

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public float getPositionZ() {
        return position.z;
    }

    public void setPositionX(float value) {
        position.x = value;
        updatePositionListeners();
    }

    public void setPositionY(float value) {
        position.y = value;
        updatePositionListeners();
    }

    public void setPositionZ(float value) {
        position.z = value;
        updatePositionListeners();
    }

    public float getSpeed() {
        return speed;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        updateRotationListeners();
    }

    public void addRotation(float rotation) {
        this.rotation += rotation;
        updateRotationListeners();
    }

    public float getSize() {
        return size;
    }

    public float getHalfSize() {
        return size / 2f;
    }

    public void setSize(float size) {
        this.size = size;
    }

}
