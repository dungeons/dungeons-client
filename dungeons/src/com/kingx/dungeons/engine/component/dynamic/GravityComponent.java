package com.kingx.dungeons.engine.component.dynamic;

public class GravityComponent extends AbstractComponent {
    public float mass;
    public MoveComponent move;
    private boolean falling = false;
    private boolean jumping = false;

    public GravityComponent(float mass, MoveComponent move) {
        this.mass = mass;
        this.move = move;
    }

    @Override
    public String toString() {
        return "MassComponent [mass=" + mass + "]";
    }

    @Override
    public void setComponent(int id, int value) {
        mass = value / AbstractComponent.INT_TO_FLOAT;
    }

    public MoveComponent getMove() {
        return move;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

}
