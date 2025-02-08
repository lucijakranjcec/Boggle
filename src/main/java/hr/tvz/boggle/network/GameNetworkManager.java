package hr.tvz.boggle.network;

import hr.tvz.boggle.boggleapplication.BoggleApplication;
import hr.tvz.boggle.model.GameState;
import hr.tvz.boggle.model.PlayerType;
import hr.tvz.boggle.util.DialogUtils;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameNetworkManager {
    public static void sendGameState(GameState gameState) {
        if (BoggleApplication.player.equals(PlayerType.SINGLE_PLAYER)) {
            return;
        }
        try {
            Socket socket;
            if (BoggleApplication.player.equals(PlayerType.PLAYER_ONE)) {
                socket = new Socket(BoggleApplication.HOST, BoggleApplication.PLAYER_TWO_SERVER_PORT);
            } else { // PLAYER_TWO
                socket = new Socket(BoggleApplication.HOST, BoggleApplication.PLAYER_ONE_SERVER_PORT);
            }
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(gameState);
            oos.flush();
            oos.close();
            socket.close();
        } catch (java.net.ConnectException ce) {
            System.err.println("Connection refused: Opponent might not be running. Continuing locally.");
            DialogUtils.showAlert(javafx.scene.control.Alert.AlertType.WARNING,
                    "Opponent is not available. Please ensure the other instance is running.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
