package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.engine.component.ShaderComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;
import com.kingx.dungeons.graphics.Colors;
import com.kingx.dungeons.graphics.Shader;

public class Background extends ConcreteEntity {

    private final PositionComponent positionComponent;
    private final ShaderComponent shader;
    private final TextureComponent textures;

    public Background(World world, Vector3 p, float width, float height) {
        super(world);

        positionComponent = new PositionComponent(p.x, p.y, p.z);
        shader = new ShaderComponent(Shader.getShader("normal"));
        textures = new TextureComponent("nighttime", "day", Colors.AVATAR);
        SizeComponent sizeComponent = new SizeComponent(width, height);

        bag.add(positionComponent);
        bag.add(sizeComponent);
        bag.add(shader);
        bag.add(textures);
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public ShaderComponent getShader() {
        return shader;
    }

    public TextureComponent getTextures() {
        return textures;
    }

}
