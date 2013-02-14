package com.kingx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.kingx.dungeons.entity.Entity;
import com.kingx.dungeons.entity.ai.AvatarBehavior;
import com.kingx.dungeons.entity.ai.PlayerControlledBehavior;

public final class Input {

    private final static Input instance = new Input();

    private Input() {
        Gdx.input.setInputProcessor(im);
    };

    public static Input getInstance() {
        return instance;
    }

    private InputMultiplexer im = new InputMultiplexer();

    public PlayerControlledBehavior getControlledEntity(Entity puppet) {
        // TODO this could easily be in {@link PlayerControlledBehavior}
        if (im.getProcessors().size < players.length) {
            PlayerControlledBehavior player = players[im.getProcessors().size];
            player.setPuppet(puppet);
            im.addProcessor(player);
            return player;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private PlayerControlledBehavior[] players = new PlayerControlledBehavior[] { new AvatarBehavior() };

    /*
     * private static final int MOVE_FACTOR = 5; private static final int ROTATE_FACTOR = 1;
     * 
     * @Override public boolean keyDown(int keycode) {
     * 
     * switch (keycode) { case Keys.W: Actor.getActor().moveY(MOVE_FACTOR); break; case Keys.S: Actor.getActor().moveY(-MOVE_FACTOR); break; case Keys.A:
     * Actor.getActor().moveX(-MOVE_FACTOR); break; case Keys.D: Actor.getActor().moveX(MOVE_FACTOR); break; case Keys.Q:
     * Actor.getActor().rotate(-MOVE_FACTOR); break; case Keys.E: Actor.getActor().rotate(MOVE_FACTOR); break; } return false; }
     * 
     * @Override public boolean keyUp(int keycode) { switch (keycode) { case Keys.W: Actor.getActor().moveY(-MOVE_FACTOR); break; case Keys.S:
     * Actor.getActor().moveY(MOVE_FACTOR); break; case Keys.A: Actor.getActor().moveX(MOVE_FACTOR); break; case Keys.D:
     * Actor.getActor().moveX(-MOVE_FACTOR); break; case Keys.Q: Actor.getActor().rotate(MOVE_FACTOR); break; case Keys.E:
     * Actor.getActor().rotate(-MOVE_FACTOR); break; } return false; }
     */

}
