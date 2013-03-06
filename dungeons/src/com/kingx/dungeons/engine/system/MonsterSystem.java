package com.kingx.dungeons.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.component.MonsterComponent;
import com.kingx.dungeons.engine.component.MoveComponent;
import com.kingx.dungeons.engine.component.PositionComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.geom.Collision;
import com.kingx.dungeons.geom.MazePoly;
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
    private final float[] verts;
    private long timestamp;

    public MonsterSystem(MazePoly mazeMesh) {
        super(Aspect.getAspectForAll(PositionComponent.class, SpeedComponent.class, MoveComponent.class, MonsterComponent.class));
        verts = mazeMesh.getVerts();
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = postionMapper.get(e);
        MonsterComponent monster = monsterMapper.get(e);
        PositionComponent playerPos = postionMapper.get(App.getPlayer().getEntity());
        ShaderComponent shader = shaderMapper.getSafe(e);

        Ray ray = getRay(playerPos.vector, position.vector);

        if (distance(playerPos.vector, position.vector) <= monster.alertRadius) {
            if (Collision.intersectRayTrianglesBetweenPoints(ray, verts, playerPos.vector, position.vector)) {
                shader.color = Colors.ZOMBIE_NORMAL.color;
            } else {
                shader.color = Colors.ZOMBIE_ALARM.color;
            }
        } else {
            shader.color = Colors.ZOMBIE_NORMAL.color;
        }
    }

    private Ray getRay(Vector3 a, Vector3 b) {
        return new Ray(a, b.cpy().sub(a));
    }

    private float distance(Vector3 a, Vector3 b) {
        float tx = a.x - b.x;
        float ty = a.y - b.y;
        return (float) Math.sqrt(tx * tx + ty * ty);
    }

    /**
     * Called before processing of entities begins.
     */
    @Override
    protected void begin() {
        timestamp = System.currentTimeMillis();
    }

    /**
     * Called after the processing of entities ends.
     */
    @Override
    protected void end() {
        System.out.println((System.currentTimeMillis() - timestamp) + " ms");
    }
}
