package com.kingx.dungeons.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.kingx.artemis.World;
import com.kingx.dungeons.App;
import com.kingx.dungeons.engine.system.server.Decoder;

public class OnlineServer extends AbstractServer {

    private final String IP;
    private final int PORT;
    private DataOutputStream out;
    private DataInputStream in;
    private final Decoder decoderSystem;

    public OnlineServer(World world) {
        super(world);
        IP = App.SERVER.getArgument(0);
        PORT = Integer.valueOf(App.SERVER.getArgument(1));

        initServer();

        decoderSystem = new Decoder(world);
        world.setSystem(decoderSystem, true);
    }

    private void initServer() {
        Socket socket;
        try {
            socket = new Socket(IP, PORT);
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

    public void recieve(ServerCommand c) {
        decoderSystem.recieve(c);
    }

    @Override
    public void updateInternal(float delta) {
        decoderSystem.process();
    }
}
