package com.kingx.dungeons.server;

import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.system.client.HideCubeSystem;

public class HeavyComputationServer extends AbstractServer {

    private final HideCubeSystem hideCube;

    public HeavyComputationServer(World world) {
        super(world);
        hideCube = world.setSystem(new HideCubeSystem(App.getCubeManager().getBlockSides()), true);
    }

    @Override
    public void process(ClientCommand c) {
    }

    @Override
    public void updateInternal(float delta) {
        hideCube.process();
    }

}
