package com.kingx.dungeons.input;

import com.kingx.dungeons.server.ClientCommand;

public interface InputPostProcessor {

    public void process(ClientCommand c);
}
