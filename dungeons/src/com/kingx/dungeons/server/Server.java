package com.kingx.dungeons.server;

public interface Server {

    void send(ClientCommand c);

    void recieve(ServerCommand c);

    void update(float step);

}
