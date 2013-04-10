package com.kingx.dungeons;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kingx.dungeons.server.ClientCommand;

public class GameStateManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -943946409313587175L;
    private static final String path = "dungeons/data/map.dng";
    private final long seed;
    private final Replay replayHandler;

    private final GameStatus status = GameStatus.RECORD;

    public enum GameStatus {
        PLAY,
        RECORD
    }

    public GameStateManager(GameState state) {
        this(state.seed, new Replay(state.getInputSequence()));
    }

    public GameStateManager(long seed, Replay replayHandler) {
        this.seed = seed;
        this.replayHandler = replayHandler;
    }

    public void writeState() {
        // Serialize data object to a file
        final FileHandle file = Gdx.files.external(path);

        ObjectOutput out;
        try {
            out = new ObjectOutputStream(file.write(false));
            out.writeObject(new GameState(seed, replayHandler.getBuffer()));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameStateManager loadState() {
        GameState state;
        try {
            state = (GameState) new ObjectInputStream(Gdx.files.external(path).read()).readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return new GameStateManager(state);
    }

    public static GameStateManager getInstance() {
        return new GameStateManager(System.nanoTime(), new Replay());
    }

    public long getSeed() {
        return seed;
    }

    public GameStatus getStatus() {
        return status;
    }

    private static class GameState implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 8736577302087657484L;
        private final long seed;
        private final ArrayList<ClientCommand> inputSequence;

        private GameState(long seed, ArrayList<ClientCommand> inputSequence) {
            this.seed = seed;
            this.inputSequence = inputSequence;
        }

        public long getSeed() {
            return seed;
        }

        public ArrayList<ClientCommand> getInputSequence() {
            return inputSequence;
        }

    }

    private static final class Replay {

        private final ArrayList<ClientCommand> buffer;

        public Replay() {
            this(new ArrayList<ClientCommand>());
        }

        public Replay(ArrayList<ClientCommand> inputSequence) {
            this.buffer = inputSequence;
        }

        public void registerInput(ClientCommand c) {
            buffer.add(c);
        }

        public ArrayList<ClientCommand> getBuffer() {
            return buffer;
        }

    }

    public void register(ClientCommand command) {
        replayHandler.registerInput(command);
    }

}
