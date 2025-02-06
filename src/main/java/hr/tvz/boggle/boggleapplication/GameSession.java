package hr.tvz.boggle.boggleapplication;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private List<Player> players;
    private Board board;
    private int currentRound;

    public GameSession() {
        players = new ArrayList<>();
        board = new Board();
        currentRound = 1;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void nextRound() {
        currentRound++;
        board = new Board(); // Generiraj novu ploču za novi krug
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public List<Player> getPlayers() {
        return players;
    }
}

