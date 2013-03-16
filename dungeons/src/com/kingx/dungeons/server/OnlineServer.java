package com.kingx.dungeons.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import com.kingx.dungeons.engine.system.Decoder;

public class OnlineServer extends AbstractServer {

    private static final String IP = "77.240.185.52";
    private static final int PORT = 5000;
    private DataOutputStream out;
    private DataInputStream in;

    public OnlineServer(Decoder client) {
        super(client);

        Socket socket;
        try {
            socket = connect();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
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

    protected Socket connect() throws UnknownHostException, IOException {
        return new Socket(IP, PORT);
    }

    @Override
    public void process(ClientCommand c) {
        try {
            out.writeShort(c.getAction());
            out.writeLong(c.getTimestamp());
            out.writeInt(c.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
