package com.kingx.dungeons.entity.ai;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.kingx.dungeons.entity.Entity;

public final class AvatarBehavior extends PlayerControlledBehavior {

    private Vector2 moveVector = new Vector2();
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
                rotateValue += dir/10f;
                break;
        }
        return false;
    }

    @Override
    public void move(float delta) {
        this.resolveMove(moveVector.x * puppet.getSpeed(), moveVector.y * puppet.getSpeed());
        puppet.addRotation(rotateValue);
    }

    // / Unused

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}
