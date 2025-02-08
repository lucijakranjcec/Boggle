package hr.tvz.boggle.model;

import hr.tvz.boggle.core.Player;
import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    // A 2D string array representing the Boggle board state.
    private String[][] boardState;

    // Index of the current player (0 for player_one, 1 for player_two).
    private int currentPlayerIndex;

    // List of players with their scores and found words.
    private List<Player> players;

    // Remaining time in seconds for the current turn.
    private int remainingTime;

    // The current round number.
    private int roundNumber;

    /**
     * All-argument constructor.
     *
     * @param boardState the current board state
     * @param currentPlayerIndex the index of the current player
     * @param players the list of players
     * @param remainingTime the remaining time in seconds
     * @param roundNumber the current round number
     */
    public GameState(String[][] boardState, int currentPlayerIndex, List<Player> players, int remainingTime, int roundNumber) {
        this.boardState = boardState;
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
        this.remainingTime = remainingTime;
        this.roundNumber = roundNumber;
    }

    // Getters and Setters

    public String[][] getBoardState() {
        return boardState;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
