package com.kingx.dungeons.engine.concrete;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;
import com.kingx.artemis.World;
import com.kingx.dungeons.Assets;
import com.kingx.dungeons.engine.component.MeshComponent;
import com.kingx.dungeons.engine.component.TextureComponent;
import com.kingx.dungeons.engine.component.VillageComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.graphics.MeshFactory;

public class Building extends ConcreteEntity {

    private final PositionComponent position;
    private final MeshComponent mesh;
    private final TextureComponent texture;
    private final VillageComponent tag;

    public Building(World world, Vector3 p, String name, float scale) {
        super(world);

        Mesh m = MeshFactory.build(name);
        m.scale(scale, scale, scale);

        this.position = new PositionComponent(p);
        this.mesh = new MeshComponent(m);
        this.texture = new TextureComponent(Assets.getTexture(name, 0));
        this.tag = new VillageComponent();
        bag.add(position);
        bag.add(mesh);
        bag.add(texture);
        bag.add(tag);
    }
}
