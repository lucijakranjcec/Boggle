package hr.tvz.boggle.boggleapplication;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private String name;
    private int wordCount;
    private Set<String> foundWords;

    public Player(String name) {
        this.name = name;
        this.wordCount = 0;
        this.foundWords = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void incrementWordCount() {
        wordCount++;
    }

    public boolean hasFoundWord(String word) {
        return foundWords.contains(word);
    }

    public void addFoundWord(String word) {
        foundWords.add(word);
    }
}
