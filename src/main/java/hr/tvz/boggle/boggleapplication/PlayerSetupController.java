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
import java.util.Objects;

public class PlayerSetupController {

    @FXML
    private TextField playerCountField;

    @FXML
    private VBox playerNamesBox;

    @FXML
    private Button startGameButton;

    // Called when the player count field changes
    public void onPlayerCountChange() {
        playerNamesBox.getChildren().clear();
        int count;
        try {
            count = Integer.parseInt(playerCountField.getText());
        } catch (NumberFormatException e) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Please enter a valid number.");
            return;
        }
        if (count < 2 || count > 5) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Number of players must be between 2 and 5.");
            return;
        }
        // Create a text field for each player name and add a listener
        for (int i = 0; i < count; i++) {
            TextField nameField = new TextField();
            nameField.setPromptText("Enter Player " + (i + 1) + " Name");
            // Add listener so that whenever the text changes, we check if all fields are filled
            nameField.textProperty().addListener((observable, oldValue, newValue) -> checkIfAllFieldsFilled());
            playerNamesBox.getChildren().add(nameField);
        }
        // Check immediately after creating the fields
        checkIfAllFieldsFilled();
    }

    // Enable the start button if all player name fields are filled
    private void checkIfAllFieldsFilled() {
        int count = Integer.parseInt(playerCountField.getText());
        boolean allFilled = true;
        for (int i = 0; i < count; i++) {
            TextField nameField = (TextField) playerNamesBox.getChildren().get(i);
            if (nameField.getText().trim().isEmpty()) {
                allFilled = false;
                break;
            }
        }
        startGameButton.setDisable(!allFilled);
    }

    // Start the game by collecting player names and loading the game screen
    @FXML
    private void startGame() {
        int count = Integer.parseInt(playerCountField.getText());
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TextField nameField = (TextField) playerNamesBox.getChildren().get(i);
            players.add(new Player(nameField.getText()));
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/boggle/boggleapplication/game_screen.fxml"));
            VBox root = loader.load();
            BoggleController controller = loader.getController();
            controller.setPlayers(players);
            controller.startGame();
            Scene scene = new Scene(root, 700, 800);
            scene.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/hr/tvz/boggle/boggleapplication/style.css")).toExternalForm());
            Stage stage = (Stage) playerCountField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error loading the game screen: " + e.getMessage());
        }
    }
}
