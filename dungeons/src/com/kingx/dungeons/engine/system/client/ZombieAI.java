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
import com.kingx.dungeons.graphics.Colors;

public class ZombieAI extends EntityProcessingSystem {
    @Mapper
    static ComponentMapper<ZombieAIComponent> dataMapper;
    private final UpdateFilter planner;

    public ZombieAI() {
        super(Aspect.getAspectForAll(ZombieAIComponent.class));

        Selector selector = new Selector();
        ((ParentTaskController) selector.getControl()).add(new Attack());
        ((ParentTaskController) selector.getControl()).add(new Search());
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

            if (!data.seeTarget) {
                return false;
            }

            Vector3 direction = getDirection(data);

            float length = direction.len();
            return length < 1f;
        }

        @Override
        public boolean doAction(Entity entity) {
            data.texture.setTint(data.alertColor);
            data.entityMove.vector.set(0, 0, 0);
            App.getPlayer().getEntity().getComponent(HealthComponent.class).decrees(1);

            if (App.DEBUG != null) {
                data.texture.setTint(Colors.DEBUG_ZOMBIE_ATTACK);
            }
            return true;
        }
    }

    private static class Search extends LeafTask {

        private ZombieAIComponent data;

        @Override
        public boolean checkConditions(Entity entity) {
            this.data = dataMapper.get(entity);

            data.seeTarget = Collision.canSee(data.playerPosition.inWorld, data.entityPosition.inWorld, data.sight.getRadius());
            if (data.seeTarget) {
                updatePosition(data);
                data.texture.setTint(data.normalColor);

                if (App.DEBUG != null) {
                    data.texture.setTint(Colors.DEBUG_ZOMBIE_SEE);
                }
                return true;
            } else {
                if (data.targetPosition != null) {
                    float distance = getLastDirection(data).len();
                    if (distance > 0.1f) {

                        if (App.DEBUG != null) {
                            data.texture.setTint(data.normalColor);
                            data.texture.setTint(Colors.DEBUG_ZOMBIE_SEARCH);
                        }
                        return true;
                    }
                    data.targetPosition = null;
                }
            }

            return false;
        }

        @Override
        public boolean doAction(Entity entity) {

            data.entityMove.vector.set(getLastDirection(data)).nor();
            data.entitySpeed.setCurrent(data.entitySpeed.turbo);

            return true;
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

            if (App.DEBUG != null) {
                data.texture.setTint(Colors.DEBUG_ZOMBIE_IDLE);
            }
            return true;
        }

        private Vector3 getNewDirection() {
            return new Vector3(App.rand.nextFloat() - 0.5f, App.rand.nextFloat() - 0.5f, 0f).nor();
        }

    }

    public static boolean canSee(Ray ray, float[] verts, ZombieAIComponent data) {
        if (Collision.distance(data.playerPosition.inWorld, data.entityPosition.inWorld) <= data.sight.getRadius()) {
            if (!Collision.intersectRayTrianglesBetweenPoints(ray, verts, data.playerPosition.inWorld, data.entityPosition.inWorld)) {
                return true;
            }
        }
        return false;
    }

    public static Vector3 getDirection(ZombieAIComponent data) {
        return data.playerPosition.inWorld.cpy().sub(data.entityPosition.inWorld);
    }

    public static Vector3 getLastDirection(ZombieAIComponent data) {
        return data.targetPosition.cpy().sub(data.entityPosition.inWorld);
    }

    public static void updatePosition(ZombieAIComponent data) {
        if (data.targetPosition == null) {
            data.targetPosition = new Vector3();
        }
        data.targetPosition.set(data.playerPosition.inWorld);
    }
}
