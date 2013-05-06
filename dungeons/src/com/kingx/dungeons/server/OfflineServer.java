package com.kingx.dungeons.server;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.GameStateManager;
import com.kingx.dungeons.GameStateManager.GameStatus;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.MiningComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent.MovementType;
import com.kingx.dungeons.engine.system.AnimationSystem;
import com.kingx.dungeons.engine.system.WorldRotateSystem;
import com.kingx.dungeons.engine.system.client.CollisionSystem;
import com.kingx.dungeons.engine.system.client.GravitySystem;
import com.kingx.dungeons.engine.system.client.MiningSystem;
import com.kingx.dungeons.engine.system.client.MovementSystem;
import com.kingx.dungeons.input.InputConstants;
import com.kingx.dungeons.input.Touch;

public class OfflineServer extends AbstractServer {

    private final GameStateManager gsm;

    public OfflineServer(World world, GameStateManager gsm) {
        super(world);
        this.gsm = gsm;
        world.setSystem(new MovementSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new GravitySystem());
        // world.setSystem(new ZombieAI());
        world.setSystem(new WorldRotateSystem());
        world.setSystem(new MiningSystem());
        world.setSystem(new AnimationSystem());
    }

    @Override
    public void process(ClientCommand c) {
        if (gsm.getStatus() == GameStatus.RECORD) {
            processInput(c);

            // replace timestamp with number of clocks game did to this point.
            ClockCommand clockCommand = new ClockCommand(c);
            gsm.register(clockCommand);
        }
    }

    @Override
    public void updateInternal(float delta) {
        if (gsm.getStatus() == GameStatus.REPLAY) {
            List<ClientCommand> commands = gsm.getCommands(App.getClock().getClocks());
            for (ClientCommand command : commands) {
                processInput(command);
            }
        }

        world.setDelta(delta);
        world.process();

        App.getTweenManager().update(delta);
    }

    private void processInput(ClientCommand c) {
        if (c.getAction() < 256) {
            processKey(c);
        } else {
            processTouch(c);
        }

    }

    private void processKey(ClientCommand c) {
        PositionComponent position = App.getPlayer().getPositionComponent();
        MoveComponent move = App.getPlayer().getMoveComponent();
        MiningComponent mining = App.getPlayer().getMining();
        CollisionComponent collision = App.getPlayer().getCollision();

        int mapped = getKey(c.getAction(), move.mapping);
        switch (mapped) {
            case Keys.W:
                if (position.canClimb()) {
                    move.vector.y = c.getValue() == 0 ? 0 : 1;
                    if (c.getValue() == 1) {
                        position.setMovementType(MovementType.CLIMB);
                    }
                }
                break;
            case Keys.S:
                if (position.canClimb()) {
                    move.vector.y = c.getValue() == 0 ? 0 : -1;
                    if (c.getValue() == 1) {
                        position.setMovementType(MovementType.CLIMB);
                    }
                }
                break;
            case Keys.A:
                move.vector.x += c.getValue() == 0 ? 1 : -1;
                break;
            case Keys.D:
                move.vector.x += c.getValue() == 0 ? -1 : 1;
                break;
            case Keys.SPACE:
                if ((position.getMovementType() == MovementType.CLIMB || collision.getStandingOnABlock() != null) && c.getValue() == 1) {
                    if (!position.isAnimation()) {
                        move.vector.y = 1.5f;
                        position.setMovementType(MovementType.JUMP);
                    }
                }
                break;
            case Keys.E:
                System.out.println(position.getMovementType());
                if (position.getMovementType() == MovementType.WALK) {
                    mining.setMining(c.getValue() != 0);
                }
                break;
            case Keys.Q:
                if (c.getValue() == 0) {
                    Assets.switchAtlas();
                }
                break;
        }
    }

    private int getKey(int keycode, Map<Integer, Integer> mapping) {
        Integer mapped = mapping.get(keycode);
        return mapped == null ? keycode : mapped;
    }

    Touch touch = new Touch();

    private void processTouch(ClientCommand c) {
        switch (c.getAction()) {
            case InputConstants.TOUCH_X_DOWN:
                touch.setX(c.getValue());
                touch.press();
                break;
            case InputConstants.TOUCH_Y_DOWN:
                touch.setY(c.getValue());
                touch.press();
                break;
            case InputConstants.TOUCH_X_UP:
                touch.setX(c.getValue());
                touch.release();
                break;
            case InputConstants.TOUCH_Y_UP:
                touch.setY(c.getValue());
                touch.release();
                break;
        }
    }
}
