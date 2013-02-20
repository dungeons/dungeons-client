package com.kingx.dungeons.entity.ai;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.kingx.dungeons.entity.Entity;

public final class AvatarBehavior extends PlayerControlledBehavior {

    private final Vector2 moveVector = new Vector2();
    private float rotateValue = 0;

    public AvatarBehavior() {
        this(null);
    }

    public AvatarBehavior(Entity puppet) {
        super(puppet);
    }

    @Override
    public boolean keyDown(int keycode) {
        return action(keycode, 1);
    }

    @Override
    public boolean keyUp(int keycode) {
        return action(keycode, -1);
    }

    private boolean action(int keycode, int dir) {
        switch (keycode) {
            case Keys.W:
                moveVector.add(0, 1 * dir);
                break;
            case Keys.S:
                moveVector.add(0, -1 * dir);
                break;
            case Keys.A:
                moveVector.add(-1 * dir, 0);
                break;
            case Keys.D:
                moveVector.add(1 * dir, 0);
                break;
            case Keys.F:
                rotateValue += dir / 10f;
                break;
        }
        return false;
    }

    @Override
    public void move(float delta) {
        this.resolveMove(moveVector.x * puppet.getSpeed() * delta, moveVector.y * puppet.getSpeed() * delta);
        puppet.addRotation(rotateValue);
    }

    // TODO Create input for touch devices
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    // NOTE curently not planned to future use
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
