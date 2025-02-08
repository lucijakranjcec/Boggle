package hr.tvz.boggle.boggleapplication;

import hr.tvz.boggle.exception.WrongPlayerNameException;
import hr.tvz.boggle.model.PlayerType;
import hr.tvz.boggle.network.PlayerOneServerThread;
import hr.tvz.boggle.network.PlayerTwoServerThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class BoggleApplication extends Application {

    public static PlayerType player;

    private static final Logger logger = Logger.getLogger(BoggleApplication.class.getName());

    public static final int PLAYER_TWO_SERVER_PORT = 1989;
    public static final int PLAYER_ONE_SERVER_PORT = 1990;
    public static final String HOST = "localhost";

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Loading FXML file...");
            // Load the game screen FXML.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/boggle/boggleapplication/game_screen.fxml"));
            VBox root = loader.load();

            // Adding CSS styles.
            Scene scene = new Scene(root, 800, 800);
            scene.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/hr/tvz/boggle/boggleapplication/style.css")).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle(player.name());
            primaryStage.show();
            logger.info("Application started");
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Error loading FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Please provide a player type (PLAYER_ONE, PLAYER_TWO, or SINGLE_PLAYER) as the first argument.");
            System.exit(1);
        }
        String firstArgument = args[0];

        if (PlayerType.valueOf(firstArgument).equals(PlayerType.PLAYER_ONE)) {
            player = PlayerType.PLAYER_ONE;
            Thread serverStarter = new Thread(new PlayerOneServerThread());
            serverStarter.start();
        }
        else if (PlayerType.valueOf(firstArgument).equals(PlayerType.PLAYER_TWO)) {
            player = PlayerType.PLAYER_TWO;
            Thread serverStarter = new Thread(new PlayerTwoServerThread());
            serverStarter.start();
        }
        else if (PlayerType.valueOf(firstArgument).equals(PlayerType.SINGLE_PLAYER)) {
            player = PlayerType.SINGLE_PLAYER;
            // No networking threads in single player mode.
        }
        else {
            throw new WrongPlayerNameException("The game was started with the player type: " + firstArgument
                    + ", but only PLAYER_ONE, PLAYER_TWO, or SINGLE_PLAYER are allowed.");
        }
        launch(args);
    }
}
