package hr.tvz.boggle.boggleapplication;

import javafx.scene.control.Alert;

public class AlertHelper {
    // Static method to show an alert message
    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }
}
