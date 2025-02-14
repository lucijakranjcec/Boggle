package hr.tvz.boggle.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int score;
    private Set<String> foundWords;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.foundWords = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean hasFoundWord(String word) {
        return foundWords.contains(word);
    }

    public void addFoundWord(String word) {
        foundWords.add(word);
    }

    public int calculateWordScore(String word) {
        int length = word.length();
        if (length == 3) return 1;
        if (length == 4) return 2;
        if (length == 5) return 3;
        if (length == 6) return 4;
        if (length == 7) return 5;
        return 6;
    }
}
