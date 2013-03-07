package com.kingx.dungeons.engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Mesh;

public class MeshComponent extends Component {
    public Mesh mesh;

    public MeshComponent(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public String toString() {
        return "MeshComponent [mesh=" + mesh + "]";
    }

}
