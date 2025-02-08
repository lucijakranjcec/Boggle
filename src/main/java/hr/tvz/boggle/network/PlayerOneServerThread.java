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

public class PlayerOneServerThread implements Runnable {
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(BoggleApplication.PLAYER_ONE_SERVER_PORT)) {
            System.out.println("Player One Server listening on port: " + serverSocket.getLocalPort());
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
            // Attempt to read the game state sent by the opponent.
            Object obj = ois.readObject();
            if (obj instanceof GameState) {
                GameState gameState = (GameState) obj;
                Platform.runLater(() -> BoggleController.updateGameStateFromNetwork(gameState));
            }
        } catch (EOFException eof) {
            // End-of-stream reached: this is common when the client closes the socket.
            System.err.println("PlayerOneServerThread: End of stream reached: " + eof.getMessage());
        } catch (SocketException se) {
            // SocketException may occur if the connection is aborted.
            System.err.println("PlayerOneServerThread: SocketException: " + se.getMessage());
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
