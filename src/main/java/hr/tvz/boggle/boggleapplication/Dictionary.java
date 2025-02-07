package hr.tvz.boggle.boggleapplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> words;

    // Load words from the specified file
    public Dictionary(String filePath) throws IOException {
        words = new HashSet<>();
        loadWords(filePath);
    }

    // Read each line from the file and add it to the set (in uppercase)
    private void loadWords(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.toUpperCase());
        }
        reader.close();
    }

    public boolean isValidWord(String word) {
        return words.contains(word.toUpperCase().trim());
    }
}
