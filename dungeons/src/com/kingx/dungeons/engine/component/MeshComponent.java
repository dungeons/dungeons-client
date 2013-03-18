package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.Mesh;
import com.kingx.artemis.Component;

public class MeshComponent extends Component {

    private final Mesh mesh;

    public MeshComponent(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mesh getMesh() {
        return mesh;
    }

}
