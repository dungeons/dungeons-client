package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;

public class WorldRotateComponent extends Component {

    private int previewView;
    private final PositionComponent position;
    private final SizeComponent size;

    public WorldRotateComponent(int previewView, PositionComponent position, SizeComponent size) {
        this.previewView = previewView;
        this.position = position;
        this.size = size;
    }

    public int getPreviewView() {
        return previewView;
    }

    public void setPreviewView(int previewView) {
        this.previewView = previewView;
    }

    public PositionComponent getPosition() {
        return position;
    }

    public SizeComponent getSize() {
        return size;
    }

}
