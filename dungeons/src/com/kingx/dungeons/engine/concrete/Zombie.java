package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.SpeedComponent;
import com.kingx.dungeons.engine.component.ZombieAIComponent;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.RotationComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.engine.tags.GeometryRenderTag;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Zombie extends ConcreteEntity {

    public Zombie(World world, Vector3 position, float size, float speed) {
        super(world);

        PositionComponent zombiePosition = new PositionComponent(position);
        PositionComponent playerPosition = App.getPlayer().getEntity().getComponent(PositionComponent.class);
        SpeedComponent zombieSpeed = new SpeedComponent(speed);
        MoveComponent zombieMove = new MoveComponent(0, 0);
        ShaderComponent shader = new ShaderComponent(Shader.getShader("normal"), Colors.ZOMBIE_NORMAL, Assets.getTexture("zombie", 0));

        bag.add(zombiePosition);
        bag.add(new RotationComponent(1, 0, 0));
        bag.add(zombieSpeed);
        bag.add(zombieMove);
        bag.add(new SizeComponent(size));
        bag.add(shader);
        bag.add(new GeometryRenderTag());
        bag.add(new ZombieAIComponent(zombiePosition, playerPosition, zombieSpeed, zombieMove, shader, Colors.ZOMBIE_NORMAL, Colors.ZOMBIE_ALARM, 4f));
    }
}
