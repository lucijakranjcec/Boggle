package hr.tvz.boggle.util;

import javafx.scene.control.Alert;

public class DialogUtils {
    // Static method to show an alert message
    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }
}
