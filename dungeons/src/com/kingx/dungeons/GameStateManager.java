package com.kingx.dungeons;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.kingx.dungeons.server.ClientCommand;

public class GameStateManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -943946409313587175L;
    private static final String path = "data/gamestate.dng";
    private final long seed;
    private final Replay replayHandler;

    public GameStateManager(GameState state) {
        this(state.seed, state.getInputSequence());
    }

    public GameStateManager(long seed, Replay replayHandler) {
        this.seed = seed;
        this.replayHandler = replayHandler;
    }

    public void writeState() {
        // Serialize data object to a file
        GameState state = new GameState(seed, replayHandler.getBuffer());
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(state);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameStateManager loadState() {
        GameState state = (GameState) new ObjectInputStream(new FileHandle(path).read()).readObject();
        return new GameStateManager(state);
    }

    public static GameStateManager getInstance(Replay replay) {
        return new GameStateManager(System.nanoTime(), replay);
    }

    public long getSeed() {
        return seed;
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

}
