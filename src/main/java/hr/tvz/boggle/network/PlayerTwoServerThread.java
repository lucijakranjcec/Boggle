package hr.tvz.boggle.network;

import hr.tvz.boggle.boggleapplication.BoggleApplication;
import hr.tvz.boggle.boggleapplication.BoggleController;
import hr.tvz.boggle.model.GameState;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;

public class PlayerTwoServerThread implements Runnable {
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(BoggleApplication.PLAYER_TWO_SERVER_PORT)) {
            System.out.println("Player Two Server listening on port: " + serverSocket.getLocalPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Platform.runLater(() -> processClient(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            Object obj = ois.readObject();
            if (obj instanceof GameState) {
                GameState gameState = (GameState) obj;
                Platform.runLater(() -> BoggleController.updateGameStateFromNetwork(gameState));
            }
        } catch (EOFException eof) {
            System.err.println("PlayerTwoServerThread: End of stream reached: " + eof.getMessage());
        } catch (SocketException se) {
            System.err.println("PlayerTwoServerThread: SocketException: " + se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
