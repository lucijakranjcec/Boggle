package hr.tvz.boggle.boggleapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class BoggleApplication extends Application {

    private static final Logger logger = Logger.getLogger(BoggleApplication.class.getName());

    @Override
    public void start(Stage primaryStage) {

        try {
            logger.info("Loading FXML file...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/boggle/boggleapplication/player_setup.fxml"));
            VBox root = loader.load();

            // adding CSS styles
            Scene scene = new Scene(root, 700, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/hr/tvz/boggle/boggleapplication/style.css")).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Boggle Game");
            primaryStage.show();
            logger.info("Application started");
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Error loading FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

