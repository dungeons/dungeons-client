package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;

public class HealthComponent extends Component {
    private int current;
    private int max;

    public HealthComponent(int max) {
        this.max = max;
        this.current = max;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void decrees(int i) {
        this.current -= i;
    }

}
