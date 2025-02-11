package hr.tvz.boggle.network;

import hr.tvz.boggle.model.GameMove;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

public class GetLastGameMoveThread extends GameMoveThread implements Runnable {
    private Label label;

    public GetLastGameMoveThread(Label label) {
        this.label = label;
    }

    @Override
    public void run() {
        GameMove lastGameMove = getLastGameMoveFromFile();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        String formattedDateTime = lastGameMove.getLocalDateTime().format(formatter);

        label.setText("Last game move: "
                + lastGameMove.getPlayerName() + ", word: "
                + lastGameMove.getWord() + ", "
                + formattedDateTime);
    }
}
