package hr.tvz.boggle.util;

import javafx.scene.control.Alert;

public class DialogUtils {

    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }

}
