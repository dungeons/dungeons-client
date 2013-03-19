package com.kingx.dungeons.engine.system.client;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.kingx.artemis.Aspect;
import com.kingx.artemis.ComponentMapper;
import com.kingx.artemis.Entity;
import com.kingx.artemis.annotations.Mapper;
import com.kingx.artemis.systems.EntityProcessingSystem;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.ai.controller.ParentTaskController;
import com.kingx.dungeons.engine.ai.task.LeafTask;
import com.kingx.dungeons.engine.ai.task.Selector;
import com.kingx.dungeons.engine.ai.task.UpdateFilter;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.ZombieAIComponent;
import com.kingx.dungeons.geom.Collision;

public class ZombieAI extends EntityProcessingSystem {
    @Mapper
    static ComponentMapper<ZombieAIComponent> dataMapper;
    private final UpdateFilter planner;

    public ZombieAI() {
        super(Aspect.getAspectForAll(ZombieAIComponent.class));

        Selector selector = new Selector();
        ((ParentTaskController) selector.getControl()).add(new Attack());
        ((ParentTaskController) selector.getControl()).add(new Search(App.getMaze().getVerts()));
        ((ParentTaskController) selector.getControl()).add(new Idle());
        planner = new UpdateFilter(selector, 1);
    }

    @Override
    protected void process(Entity e) {
        planner.start(e);
        planner.doAction(e);
    }

    private static class Attack extends LeafTask {

        private ZombieAIComponent data;

        @Override
        public boolean checkConditions(Entity entity) {
            this.data = dataMapper.get(entity);
            if (data.target == null) {
                return false;
            }

            this.data = dataMapper.get(entity);
            updateTarget(data);
            updateTargetDistance(data);
            float length = data.targetDist.len();

            return length < 1f;
        }

        @Override
        public boolean doAction(Entity entity) {
            data.texture.setTint(data.alertColor);
            data.entityMove.vector.set(0, 0, 0);
            App.getPlayer().getEntity().getComponent(HealthComponent.class).decrees(1);
            return true;
        }
    }

    private static class Search extends LeafTask {

        private ZombieAIComponent data;
        private final float[] verts;

        private Search(float[] verts) {
            this.verts = verts;
        }

        @Override
        public boolean checkConditions(Entity entity) {
            this.data = dataMapper.get(entity);

            Ray ray = getRay(data.playerPosition.vector, data.entityPosition.vector);

            if (Collision.distance(data.playerPosition.vector, data.entityPosition.vector) <= data.alertRadius) {
                return !Collision.intersectRayTrianglesBetweenPoints(ray, verts, data.playerPosition.vector, data.entityPosition.vector);
            }

            return false;
        }

        @Override
        public boolean doAction(Entity entity) {

            updateTarget(data);
            updateTargetDistance(data);

            data.entityMove.vector.set(data.targetDist).nor();
            data.entitySpeed.setCurrent(data.entitySpeed.turbo);
            data.texture.setTint(data.normalColor);
            return true;
        }

        private Ray getRay(Vector3 a, Vector3 b) {
            return new Ray(a, b.cpy().sub(a));
        }

    }

    private static class Idle extends LeafTask {

        private ZombieAIComponent data;
        private int counter;

        @Override
        public boolean checkConditions(Entity entity) {
            this.data = dataMapper.get(entity);
            return true;
        }

        @Override
        public boolean doAction(Entity e) {
            counter++;
            if (counter > 40) {
                counter = 0;
                data.entityMove.vector = getNewDirection();
            }
            data.entitySpeed.setCurrent(data.entitySpeed.normal);
            data.texture.setTint(data.normalColor);
            return true;
        }

        private Vector3 getNewDirection() {
            return new Vector3(App.rand.nextFloat() - 0.5f, App.rand.nextFloat() - 0.5f, 0f).nor();
        }

    }

    public static void updateTargetDistance(ZombieAIComponent data) {
        data.targetDist.set(data.target.cpy().sub(data.entityPosition.vector));

    }

    public static void updateTarget(ZombieAIComponent data) {
        data.target = data.playerPosition.vector.cpy();
    }
}
