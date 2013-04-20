package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.Component;
import com.kingx.dungeons.Assets;

public class BackgroundComponent extends Component {

    private final int SIDES = 8;
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
        float angle = CORNER_ANGLE * (i / 2);
        float angle2 = angle;
        angle2 += i % 2 == 0 ? 2 * CORNER_ANGLE : 0;
        TextureComponent texture = new TextureComponent(Assets.getTexture(name, i), angle);
        float x = (float) (position.x + Math.cos(MathUtils.degreesToRadians * angle2) * (texture.getWidth() / 2f));
        float z = (float) (position.z - Math.sin(MathUtils.degreesToRadians * angle2) * (texture.getWidth() / 2f));
        return new SpriteComponent(x, position.y + texture.getHeight() / 2f, z, texture);
    }

    public TextureComponent getTexture(int i) {
        return sides[i].getTexture();
    }

    public SpriteComponent[] getSides() {
        return sides;
    }

}
