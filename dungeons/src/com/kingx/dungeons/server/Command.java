package com.kingx.dungeons.server;

import java.nio.ByteBuffer;

public abstract class Command {

    protected final ByteBuffer buffer;

    protected Command(int size) {
        this.buffer = ByteBuffer.allocate(size);
    }

    protected int readInt() {
        return buffer.getInt();
    }

    protected void writeInt(int v) {
        buffer.putInt(v);
    }

    public int getSize() {
        return buffer.capacity();
    }

}
