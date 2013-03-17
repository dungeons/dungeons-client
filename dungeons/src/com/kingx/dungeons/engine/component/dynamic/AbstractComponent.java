package com.kingx.dungeons.engine.component.dynamic;

import com.kingx.artemis.Component;

public abstract class AbstractComponent extends Component {

    public static final float INT_TO_FLOAT = 1000f;

    public abstract void setComponent(int id, int value);

}
