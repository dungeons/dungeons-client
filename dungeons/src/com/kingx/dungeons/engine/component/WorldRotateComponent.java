package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;
import com.kingx.dungeons.engine.component.dynamic.MoveComponent;
import com.kingx.dungeons.engine.component.dynamic.PositionComponent;
import com.kingx.dungeons.engine.component.dynamic.SizeComponent;

public class WorldRotateComponent extends Component {

    private final PositionComponent position;
    private final MoveComponent move;
    private final SizeComponent size;

    public WorldRotateComponent(PositionComponent position, MoveComponent move, SizeComponent size) {
        this.position = position;
        this.move = move;
        this.size = size;
    }

    public PositionComponent getPosition() {
        return position;
    }

    public MoveComponent getMove() {
        return move;
    }

    public SizeComponent getSize() {
        return size;
    }

}
