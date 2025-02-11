package hr.tvz.boggle;

import hr.tvz.boggle.exception.WrongPlayerNameException;
import hr.tvz.boggle.model.PlayerType;
import hr.tvz.boggle.network.PlayerOneServerThread;
import hr.tvz.boggle.network.PlayerTwoServerThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class BoggleApplication extends Application {

    public static PlayerType player;
    public static Stage mainStage;
    public static final int PLAYER_TWO_SERVER_PORT = 1989;
    public static final int PLAYER_ONE_SERVER_PORT = 1990;
    public static final String HOST = "localhost";

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/tvz/boggle/boggleapplication/game_screen.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 950);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/hr/tvz/boggle/boggleapplication/style.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle(player.name());
        stage.show();
    }

    public static void main(String[] args) {

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
        }
        else {
            throw new WrongPlayerNameException("The game was started with the player type: " + firstArgument
                    + ", but only PLAYER_ONE, PLAYER_TWO, or SINGLE_PLAYER are allowed.");
        }
        launch(args);
    }
}
