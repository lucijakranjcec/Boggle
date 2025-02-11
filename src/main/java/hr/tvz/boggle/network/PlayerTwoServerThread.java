package hr.tvz.boggle.network;

import hr.tvz.boggle.BoggleApplication;
import hr.tvz.boggle.BoggleController;
import hr.tvz.boggle.model.GameState;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;

public class PlayerTwoServerThread implements Runnable {
    @Override
    public void run() {
        playerTwoAcceptRequests();
    }

    private static void playerTwoAcceptRequests() {
        try (ServerSocket serverSocket = new ServerSocket(BoggleApplication.PLAYER_TWO_SERVER_PORT)) {
            System.out.println("Player Two Server listening on port: " + serverSocket.getLocalPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player Two: Client connected from port: " + clientSocket.getPort());
                Platform.runLater(() -> processSerializableClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            GameState gameState = (GameState) ois.readObject();
            Platform.runLater(() -> BoggleController.updateGameStateFromNetwork(gameState));
            System.out.println("Player Two received the game state!");
            oos.writeObject("Player Two received the game state - confirmation!");
        } catch (EOFException eof) {
            System.err.println("PlayerTwoServerThread: End of stream reached: " + eof.getMessage());
        } catch (SocketException se) {
            System.err.println("PlayerTwoServerThread: SocketException: " + se.getMessage());
        } catch (IOException | ClassNotFoundException e) {
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
