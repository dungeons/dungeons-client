package com.kingx.dungeons.server;

public interface Server {

    void send(Command c);

    Command recieve();
}
