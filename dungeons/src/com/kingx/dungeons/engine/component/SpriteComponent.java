package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;

public class SpriteComponent extends Component {
    private final PositionComponent positionComponent;
    private final TextureComponent texture;
    private final SizeComponent sizeComponent;

    public SpriteComponent(float x, float y, float z, TextureComponent texture) {
        positionComponent = new PositionComponent(x, y, z);
        this.texture = texture;
        sizeComponent = new SizeComponent(this.texture.getWidth(), this.texture.getHeight());
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public TextureComponent getTexture() {
        return texture;
    }

    public SizeComponent getSizeComponent() {
        return sizeComponent;
    }

}
