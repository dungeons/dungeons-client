package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Component;
import com.kingx.dungeons.Assets;

public class BackgroundComponent extends Component {

    private final int SIDES = 4;
    private final float CORNER_ANGLE = 90;
    private final SpriteComponent[] sides = new SpriteComponent[SIDES];
    private final String name;
    private final Vector3 position;

    public BackgroundComponent(Vector3 position, String name) {
        this.name = name;
        this.position = position;
        for (int i = 0; i < sides.length; i++) {
            sides[i] = createSide(i);
        }
    }

    private SpriteComponent createSide(int i) {
        TextureComponent texture = new TextureComponent(Assets.getTexture(name, i), CORNER_ANGLE * i);
        return new SpriteComponent(position.x, position.y + texture.getHeight() / 2f, position.z, texture);
    }

    public TextureComponent getTexture(int i) {
        return sides[i].getTexture();
    }

    public SpriteComponent[] getSides() {
        return sides;
    }

}
