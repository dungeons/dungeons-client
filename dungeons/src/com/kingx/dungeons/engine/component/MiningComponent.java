package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;

public class MiningComponent extends Component {

    private boolean mining = false;

    public MiningComponent(boolean mining) {
        this.mining = mining;
    }

    public boolean isMining() {
        return mining;
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }

}