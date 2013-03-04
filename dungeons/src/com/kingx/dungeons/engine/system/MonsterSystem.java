package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.MonsterComponent;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.graphics.Colors;

public class MonsterSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> postionMapper;
    @Mapper
    ComponentMapper<SpeedComponent> speedMapper;
    @Mapper
    ComponentMapper<ShaderComponent> shaderMapper;
    @Mapper
    ComponentMapper<MonsterComponent> monsterMapper;

    public MonsterSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, SpeedComponent.class, MoveComponent.class, MonsterComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        SpeedComponent speed = speedMapper.get(e);
        MonsterComponent monster = monsterMapper.get(e);

        try {
            ShaderComponent shader = shaderMapper.getSafe(e);
            // App.getMaze().
            if (distance(App.getPlayer().getEntity(), e) <= monster.alertRadius) {
                shader.color = Colors.ZOMBIE_ALARM.color;
            } else {
                shader.color = Colors.ZOMBIE_NORMAL.color;
            }
            /*
                    position.x += moveVector.vector.x * speed.speed * world.delta;
                    position.y += moveVector.vector.y * speed.speed * world.delta;

                    if (cameraMapper.has(e)) {
                        FollowCameraComponent cameraComponent = cameraMapper.get(e);
                        cameraComponent.camera.position.x = position.x;
                        cameraComponent.camera.position.y = position.y - 2f;
                        cameraComponent.camera.lookAt(position.x, position.y, position.z);
                        cameraComponent.camera.position.z = cameraComponent.height;
                    }*/
        } catch (ComponentException ex) {
            ex.printStackTrace();
        }
    }

    private float distance(Entity a, Entity b) throws ComponentException {
        if (postionMapper.has(a) && postionMapper.has(b)) {
            PositionComponent apos = postionMapper.get(a);
            PositionComponent bpos = postionMapper.get(b);
            float tx = apos.x - bpos.x;
            float ty = apos.y - bpos.y;
            return (float) Math.sqrt(tx * tx + ty * ty);
        }
        throw new ComponentException();
    }
}
