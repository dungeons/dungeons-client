package com.kingx.dungeons.input;

import com.kingx.dungeons.App;
import com.kingx.dungeons.server.ClientCommand;

public class RegularInputProcessor implements InputPostProcessor {

    @Override
    public void process(ClientCommand c) {
        System.out.println("proces");
        App.getServer().send(c);
    }

}
