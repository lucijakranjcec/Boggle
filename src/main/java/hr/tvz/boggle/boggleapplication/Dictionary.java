package hr.tvz.boggle.boggleapplication;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> words;

    public Dictionary(String filePath) throws IOException {
        words = new HashSet<>();
        loadWords(filePath);
    }

    private void loadWords(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String word;
            while ((word = br.readLine()) != null) {
                words.add(word.toUpperCase());
            }
        }
    }

    public boolean isValidWord(String word) {
        String normalizedWord = word.toUpperCase().trim(); // Trim spaces and convert to upper case
        System.out.println("Checking word: " + normalizedWord); // Debugging print
        return words.contains(normalizedWord);
    }
}
