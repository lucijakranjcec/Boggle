package hr.tvz.boggle.util;

import hr.tvz.boggle.core.Player;

import java.util.List;

public class GameResultsHelper {

    public static String getWinnerDetails(List<Player> players) {
        int highestScore = 0;
        for (Player player : players) {
            if (player.getWordCount() > highestScore) {
                highestScore = player.getWordCount();
            }
        }
        int count = 0;
        for (Player player : players) {
            if (player.getWordCount() == highestScore) {
                count++;
            }
        }
        if (count > 1) {
            return "Winner: It's a draw!";
        } else {
            for (Player player : players) {
                if (player.getWordCount() == highestScore) {
                    return "Winner: " + player.getName();
                }
            }
        }
        return "Winner: Unknown";
    }

    public static String getScores(List<Player> players) {
        StringBuilder scores = new StringBuilder();
        for (Player player : players) {
            scores.append(player.getName())
                    .append(": ")
                    .append(player.getWordCount())
                    .append(" points\n");
        }
        return "Final Scores:\n" + scores;
    }
}
