package com.kingx.dungeons.server;

import java.io.OutputStream;
import java.net.Socket;

import com.kingx.dungeons.Logic;

public class OnlineServer extends AbstractServer {

    private OutputStream out;

    public OnlineServer(Logic logic) {
        super(logic);
        Socket kkSocket;
        try {
            kkSocket = new Socket("localhost", 5000);
            out = kkSocket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void send(Command c) {
        try {
            out.write(c.buffer.array());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Command recieve() {
        return null;
    }

}
