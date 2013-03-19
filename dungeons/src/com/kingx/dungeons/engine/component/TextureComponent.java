package com.kingx.dungeons.engine.component;

import com.badlogic.gdx.graphics.Color;
import com.kingx.artemis.Component;

public class TextureComponent extends Component {
    private String healty;
    private String damaged;
    private Color tint;

    public TextureComponent(String healty, String damaged, Color tint) {
        this.healty = healty;
        this.damaged = damaged;
        this.tint = tint;
    }

    public String getHealty() {
        return healty;
    }

    public void setHealty(String healty) {
        this.healty = healty;
    }

    public String getDamaged() {
        return damaged;
    }

    public void setDamaged(String damaged) {
        this.damaged = damaged;
    }

    public Color getTint() {
        return tint;
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

}
