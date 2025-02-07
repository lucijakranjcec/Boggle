package hr.tvz.boggle.model;

import hr.tvz.boggle.boggleapplication.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    // A 2D string array representing the Boggle board state
    private String[][] boardState;

    // Index of the current player
    private int currentPlayerIndex;

    // List of players with their scores and found words
    private List<Player> players;

    // Remaining time in seconds for the current turn
    private int remainingTime;
}
