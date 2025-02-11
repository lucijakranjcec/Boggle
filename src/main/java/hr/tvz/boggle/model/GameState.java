package hr.tvz.boggle.model;

import hr.tvz.boggle.core.Player;
import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private String[][] boardState;

    private int currentPlayerIndex;

    private List<Player> players;

    private int remainingTime;

    private int roundNumber;

    public GameState(String[][] boardState, int currentPlayerIndex, List<Player> players, int remainingTime, int roundNumber) {
        this.boardState = boardState;
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
        this.remainingTime = remainingTime;
        this.roundNumber = roundNumber;
    }

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
