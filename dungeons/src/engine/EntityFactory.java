package com.gamadu.spaceshipwarrior;

import javax.swing.text.Position;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.graphics.g2d.Sprite;

import engine.component.Velocity;

public class EntityFactory {

    public static Entity createPlayer(World world, float x, float y) {
        Entity e = world.createEntity();

        Position position = new Position();
        position.x = x;
        position.y = y;
        e.addComponent(position);

        Sprite sprite = new Sprite();
        sprite.name = "fighter";
        sprite.r = 93 / 255f;
        sprite.g = 255 / 255f;
        sprite.b = 129 / 255f;
        sprite.layer = Sprite.Layer.ACTORS_3;
        e.addComponent(sprite);

        Velocity velocity = new Velocity();
        velocity.vectorX = 0;
        velocity.vectorY = 0;
        e.addComponent(velocity);

        Bounds bounds = new Bounds();
        bounds.radius = 43;
        e.addComponent(bounds);

        e.addComponent(new Player());

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYER_SHIP);

        return e;
    }

}
