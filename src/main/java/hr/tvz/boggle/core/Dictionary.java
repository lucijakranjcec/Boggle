package hr.tvz.boggle.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> words;

    public Dictionary(String filePath) throws IOException {
        words = new HashSet<>();
        loadWords(filePath);
    }

    private void loadWords(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.toUpperCase());
        }
        reader.close();
    }

    public boolean isValidWord(String word) {
        if (word == null || word.trim().length() < 3) {
            return false;
        }
        return words.contains(word.toUpperCase().trim());
    }

}
