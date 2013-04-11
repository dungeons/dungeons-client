package com.kingx.dungeons;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kingx.dungeons.server.ClientCommand;
import com.kingx.dungeons.server.ClockCommand;

public class GameStateManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -943946409313587175L;
    private static final String path = "dungeons/data/state.dng";
    private final long seed;
    private final List<ClockCommand> inputSequence;

    private final GameStatus status;

    public enum GameStatus {
        REPLAY,
        RECORD
    }

    private GameStateManager(GameStatus status, long seed, List<ClockCommand> inputSequence) {
        this.status = status;
        this.seed = seed;
        this.inputSequence = inputSequence;
    }

    public void writeState() {
        if (status == GameStatus.REPLAY)
            return;
        // Serialize data object to a file
        final FileHandle file = Gdx.files.external(path);

        ObjectOutput out;
        try {
            out = new ObjectOutputStream(file.write(false));
            out.writeObject(new GameState(seed, inputSequence));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadState() {
        GameState state;
        try {
            state = (GameState) new ObjectInputStream(Gdx.files.external(path).read()).readObject();
        } catch (Exception e) {
            state = null;
            System.err.println("Replay [" + path + "] was not found.");
        }
        return state;
    }

    public static GameStateManager getInstance(GameStatus status) {
        GameStateManager instance = null;
        switch (status) {
            case RECORD:
                instance = new GameStateManager(status, System.nanoTime(), new LinkedList<ClockCommand>());
                break;
            case REPLAY:
                GameState state = GameStateManager.loadState();
                instance = state == null ? getInstance(GameStatus.RECORD) : new GameStateManager(status, state.getSeed(), state.getInputSequence());
                break;
        }
        return instance;
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
        private final List<ClockCommand> inputSequence;

        private GameState(long seed, List<ClockCommand> inputSequence) {
            this.seed = seed;
            this.inputSequence = inputSequence;
        }

        public long getSeed() {
            return seed;
        }

        public List<ClockCommand> getInputSequence() {
            return inputSequence;
        }

    }

    public void register(ClockCommand clockCommand) {
        inputSequence.add(clockCommand);
    }

    public List<ClockCommand> getReplay() {
        return inputSequence;
    }

    public List<ClientCommand> getCommands(long clocks) {
        ArrayList<ClientCommand> result = new ArrayList<ClientCommand>();
        Iterator<ClockCommand> iterator = inputSequence.iterator();
        while (iterator.hasNext()) {
            ClockCommand cc = iterator.next();
            if (cc.getTimestamp() == clocks) {
                result.add(cc);
                iterator.remove();
            }
        }
        return result;
    }
}
