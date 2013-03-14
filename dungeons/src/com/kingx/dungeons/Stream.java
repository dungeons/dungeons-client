package com.kingx.dungeons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Stream {

    private final InputStream in;
    private final OutputStream out;

    private Stream(InputStream in, OutputStream out) {
        this.in = new InputStream() {

            @Override
            public int read() throws IOException {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        this.out = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                // TODO Auto-generated method stub

            }
        };
    }

}
