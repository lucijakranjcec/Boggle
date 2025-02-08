package hr.tvz.boggle.util;

import hr.tvz.boggle.model.GameState;
import hr.tvz.boggle.util.DialogUtils;
import javafx.scene.control.Alert;

import java.io.*;

public class GamePersistenceManager {

    private static final String SAVE_FILE_PATH = "saveGame/gameSave.dat";

    /**
     * Saves the given game state to a file.
     *
     * @param gameState the current game state to save
     */
    public static void saveGame(GameState gameState) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(gameState);
            DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Game was successfully saved!");
        } catch (IOException ex) {
            ex.printStackTrace();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Failed to save the game.");
        }
    }

    /**
     * Loads the game state from a file.
     *
     * @return the loaded game state, or null if the game could not be loaded
     */
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
