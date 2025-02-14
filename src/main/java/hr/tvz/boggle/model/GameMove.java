package hr.tvz.boggle.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameMove implements Serializable {

    private static final long serialVersionUID = 3868374188484819327L;

    private String playerName;
    private String word;
    private LocalDateTime localDateTime;

    public GameMove() {
    }

    public GameMove(String playerName, String word, LocalDateTime localDateTime) {
        this.playerName = playerName;
        this.word = word;
        this.localDateTime = localDateTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

}
