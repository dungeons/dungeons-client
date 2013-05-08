package com.kingx.dungeons.engine.system.client;

import java.util.ArrayList;

import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.ShadowComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.graphics.cube.Cube;
import com.kingx.dungeons.graphics.cube.CubeRegion;

public class HideCubeSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<SightComponent> sm;

    private final ArrayList<CubeRegion> blocks;

    public HideCubeSystem(ArrayList<CubeRegion> blocks) {
        super(Aspect.getAspectForAll(SightComponent.class, ShadowComponent.class));
        this.blocks = blocks;
    }

    private boolean firstTime = true;

    @Override
    protected void process(Entity e) {
        for (CubeRegion region : blocks) {
            for (Cube[] cubes : region.getCubes()) {
                for (Cube cube : cubes) {
                    if (cube != null) {
                        cube.setHidden(!Collision.isCubeVisible(cube, (int) sm.get(e).getRadius()));
                    }
                }
            }
        }

        if (firstTime) {
            App.getCubeManager().hideInvidibleParts();
            firstTime = false;
        }

    }
}
