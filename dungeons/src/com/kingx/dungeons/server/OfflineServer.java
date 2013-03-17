package com.kingx.dungeons.server;

import com.artemis.World;
import com.badlogic.gdx.Input.Keys;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.system.CollisionSystem;
import com.kingx.dungeons.engine.system.MovementSystem;
import com.kingx.dungeons.engine.system.ai.ZombieAI;

public class OfflineServer extends AbstractServer {

    public OfflineServer(World world) {
        super(world);
        world.setSystem(new MovementSystem());
        world.setSystem(new ZombieAI());
        world.setSystem(new CollisionSystem());
    }

    @Override
    public void process(ClientCommand c) {
        processInput(c);
    }

    @Override
    public void updateInternal(float delta) {
        world.setDelta(delta);
        world.process();
    }

    private void processInput(ClientCommand c) {
        MoveComponent position = App.getPlayer().getPosition();

        System.out.println(c);
        switch (c.getAction()) {
            case Keys.W:
                position.vector.y += c.getValue() == 0 ? -1 : 1;
                break;
            case Keys.S:
                position.vector.y += c.getValue() == 0 ? 1 : -1;
                break;
            case Keys.A:
                position.vector.x += c.getValue() == 0 ? 1 : -1;
                break;
            case Keys.D:
                position.vector.x += c.getValue() == 0 ? -1 : 1;
                break;
        }
    }
}
