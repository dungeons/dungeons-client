package com.kingx.dungeons.engine.component;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.kingx.artemis.Component;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;

public class AnimationComponent extends Component {

    private final String animationClass;
    private TextureComponent texture;
    private final MoveComponent move;
    private final Map<String, Array<AtlasRegion>> animations = new HashMap<String, Array<AtlasRegion>>();
    private Array<AtlasRegion> playing;
    private int position;

    public AnimationComponent(String animationClass, TextureComponent texture) {
        this.animationClass = animationClass;
        this.texture = texture;
        this.move = null;
    }

    public AnimationComponent(String animationClass, TextureComponent texture, MoveComponent move) {
        this.animationClass = animationClass;
        this.texture = texture;
        this.move = move;
    }

    public void addAnimation(String name) {
        String wholeName = animationClass + "." + name;
        Array<AtlasRegion> textures = animations.get(wholeName);
        if (textures == null) {
            Array<AtlasRegion> anim = Assets.getTextureArray(wholeName);
            if (anim != null && anim.size > 0) {
                animations.put(wholeName, anim);
            } else {
                throw new IllegalArgumentException("Animation [" + wholeName + "] does not exist.");
            }
        } else {
            throw new IllegalArgumentException("Animation [" + wholeName + "] already exists.");
        }
    }

    public void play(String name) {
        String wholeName = animationClass + "." + name;
        if (move != null) {
            if (move.getX() < 0) {
                wholeName += ".left";
            } else if (move.getX() > 0) {
                wholeName += ".right";
            } else {
                wholeName += ".middle";
            }
        }

        System.out.println("Play: " + wholeName);

        if (playing != animations.get(wholeName)) {
            playing = animations.get(wholeName);
            if (playing == null) {
                throw new IllegalArgumentException("Animation [" + wholeName + "] does not exist.");
            }
            position = 0;
            texture.setTexture(playing.get(position));
            System.out.println(wholeName);
        }
    }

    public void next() {
        if (playing != null) {
            position++;

            if (position >= playing.size) {
                position = 0;
            }
            texture.setTexture(playing.get(position));
        }

    }

    public TextureComponent getTexture() {
        return texture;
    }

    public void setTexture(TextureComponent texture) {
        this.texture = texture;
    }

    public Array<AtlasRegion> getPlaying() {
        return playing;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
