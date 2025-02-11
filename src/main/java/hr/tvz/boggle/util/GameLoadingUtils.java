package hr.tvz.boggle.util;

import hr.tvz.boggle.model.GameState;
import javafx.scene.control.Alert;

import java.io.*;

public class GameLoadingUtils {

    private static final String SAVE_FILE_PATH = "saveGame/gameSave.dat";

    public static void saveGame(GameState gameState) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(gameState);
            DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Game was successfully saved!");
        } catch (IOException ex) {
            ex.printStackTrace();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Failed to save the game.");
        }
    }

    public static GameState loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Failed to load the game.");
            return null;
        }
    }
}
