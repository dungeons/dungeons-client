package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.AnimationComponent;
import com.kingx.dungeons.engine.component.CollisionComponent;
import com.kingx.dungeons.engine.component.HealthComponent;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SightComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.ZombieAIComponent;
import com.kingx.dungeons.engine.component.dynamic.GravityComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Zombie extends ConcreteEntity {

    public Zombie(World world, Vector3 position, float size, float speed) {
        super(world);

        PositionComponent zombiePosition = new PositionComponent(position);
        PositionComponent playerPosition = App.getPlayer().getEntity().getComponent(PositionComponent.class);
        SpeedComponent zombieSpeed = new SpeedComponent(speed);
        MoveComponent zombieMove = new MoveComponent(0, 0);
        ShaderComponent shader = new ShaderComponent(Shader.getShader("normal"));
        TextureComponent texture = new TextureComponent(Assets.getTexture("miner.walk.middle", 0));
        HealthComponent health = new HealthComponent(100);
        SightComponent sight = new SightComponent(3f);
        GravityComponent gravity = new GravityComponent(3f, zombieMove);
        CollisionComponent collision = new CollisionComponent();

        AnimationComponent animationComponent = new AnimationComponent("miner", texture, zombieMove);
        animationComponent.addAnimation("jump.left");
        animationComponent.addAnimation("jump.middle");
        animationComponent.addAnimation("jump.right");

        animationComponent.addAnimation("climb.left");
        animationComponent.addAnimation("climb.middle");
        animationComponent.addAnimation("climb.right");

        animationComponent.addAnimation("walk.left");
        animationComponent.addAnimation("walk.middle");
        animationComponent.addAnimation("walk.right");

        animationComponent.addAnimation("dig.left");
        animationComponent.addAnimation("dig.middle");
        animationComponent.addAnimation("dig.right");

        bag.add(zombiePosition);
        bag.add(new RotationComponent(0, 1, 0));
        bag.add(new SpeedComponent(speed));
        bag.add(zombieMove);
        bag.add(new SizeComponent(size));
        bag.add(shader);
        bag.add(texture);
        bag.add(health);
        bag.add(sight);
        bag.add(gravity);
        bag.add(collision);
        bag.add(animationComponent);

        bag.add(new ZombieAIComponent(zombiePosition, playerPosition, zombieSpeed, zombieMove, shader, texture, Colors.ZOMBIE_NORMAL,
                Colors.ZOMBIE_ALARM, sight));

    }
}
