package com.kingx.dungeons.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.kingx.dungeons.Logic;

public class OnlineServer extends AbstractServer {

    private OutputStream out;
    private InputStream in;

    public OnlineServer(Logic logic) {
        super(logic);
        Socket kkSocket;
        try {
            kkSocket = new Socket("77.240.185.52", 5000);
            in = kkSocket.getInputStream();
            out = kkSocket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int read;
                try {
                    while ((read = in.read()) != -1) {
                        System.out.println(read);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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
