package com.kingx.dungeons.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class OnlineServer extends AbstractServer {

    private OutputStream out;
    private InputStream in;

    public OnlineServer(Client client) {
        super(client);

        Socket socket;
        try {
            socket = new Socket("77.240.185.52", 5000);
            in = socket.getInputStream();
            out = socket.getOutputStream();
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
    public void process(ArrayList<Command> buffer) {
        for (Command c : buffer) {
            try {
                out.write(c.buffer.array());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
