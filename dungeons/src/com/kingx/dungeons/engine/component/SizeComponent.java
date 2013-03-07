package com.kingx.dungeons.engine.component;

import com.artemis.Component;

public class SizeComponent extends Component {
    public float size;

    public SizeComponent(float size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SizeComponent [size=" + size + "]";
    }

}
