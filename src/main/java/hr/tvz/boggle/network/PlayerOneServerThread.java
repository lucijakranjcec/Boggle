package hr.tvz.boggle.network;

import hr.tvz.boggle.boggleapplication.BoggleApplication;
import hr.tvz.boggle.boggleapplication.BoggleController;
import hr.tvz.boggle.model.GameState;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;

public class PlayerOneServerThread implements Runnable {
    @Override
    public void run() {
        playerOneAcceptRequests();
    }

    private static void playerOneAcceptRequests() {
        try (ServerSocket serverSocket = new ServerSocket(BoggleApplication.PLAYER_ONE_SERVER_PORT)) {
            System.out.println("Player One Server listening on port: " + serverSocket.getLocalPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player One: Client connected from port: " + clientSocket.getPort());
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
            System.out.println("Player One received the game state!");
            oos.writeObject("Player One received the game state - confirmation!");
        } catch (EOFException eof) {
            System.err.println("PlayerOneServerThread: End of stream reached: " + eof.getMessage());
        } catch (SocketException se) {
            System.err.println("PlayerOneServerThread: SocketException: " + se.getMessage());
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
