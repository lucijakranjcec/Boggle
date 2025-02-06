package hr.tvz.boggle.boggleapplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSetupController {

    @FXML
    private TextField playerCountField;

    @FXML
    private VBox playerNamesBox;

    @FXML
    private Button startGameButton;

    private List<Player> players;

    public void onPlayerCountChange() {
        playerNamesBox.getChildren().clear();
        int playerCount;

        try {
            playerCount = Integer.parseInt(playerCountField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Please enter a valid number.");
            return;
        }

        if (playerCount >= 2 && playerCount <= 5) {
            for (int i = 0; i < playerCount; i++) {
                TextField playerNameField = new TextField();
                playerNameField.setPromptText("Enter Player " + (i + 1) + " Name");
                playerNameField.getStyleClass().add("input-field");
                playerNameField.textProperty().addListener((observable, oldValue, newValue) -> checkIfAllFieldsFilled());
                playerNamesBox.getChildren().add(playerNameField);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Number of players must be between 2 and 5.");
        }
        checkIfAllFieldsFilled();
    }

    private void checkIfAllFieldsFilled() {
        int playerCount = Integer.parseInt(playerCountField.getText());
        boolean allFilled = true;

        for (int i = 0; i < playerCount; i++) {
            TextField playerNameField = (TextField) playerNamesBox.getChildren().get(i);
            if (playerNameField.getText().isEmpty()) {
                allFilled = false;
                break;
            }
        }

        startGameButton.setDisable(!allFilled);
    }

    @FXML
    private void startGame() {
        // Get player count and names, then initialize the game
        int playerCount = Integer.parseInt(playerCountField.getText());
        players = new ArrayList<>();

        // Collect player names and create Player objects
        for (int i = 0; i < playerCount; i++) {
            TextField playerNameField = (TextField) playerNamesBox.getChildren().get(i);
            String playerName = playerNameField.getText();
            players.add(new Player(playerName)); // Assuming Player class takes a name as a constructor
        }

        // Transition to the Game Screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/boggle/boggleapplication/game_screen.fxml"));
            VBox root = loader.load();

            // Pass player data to the game controller (via BoggleController)
            BoggleController controller = loader.getController();
            controller.setPlayers(players); // Pass players to the game controller

            // Start the game from BoggleController
            controller.startGame();  // Start the game after setting players

            // Create a new scene for the game screen
            Scene gameScene = new Scene(root, 700, 800);
            gameScene.getStylesheets().add(getClass().getResource("/hr/tvz/boggle/boggleapplication/style.css").toExternalForm());

            // Close the player setup window and show the game screen
            Stage stage = (Stage) playerCountField.getScene().getWindow();
            stage.setScene(gameScene);  // Set the new scene
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading the game screen: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }
}
