package hr.tvz.boggle.util;

import hr.tvz.boggle.BoggleApplication;
import hr.tvz.boggle.core.Player;
import hr.tvz.boggle.model.GameState;
import hr.tvz.boggle.model.PlayerType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MultiplayerUtils {
    public static void playerOneSendRequest(GameState gameState) {
        try (Socket clientSocket = new Socket(BoggleApplication.HOST, BoggleApplication.PLAYER_TWO_SERVER_PORT)) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            sendSerializableRequestToPlayerTwo(clientSocket, gameState);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void playerTwoSendRequest(GameState gameState) {
        try (Socket clientSocket = new Socket(BoggleApplication.HOST, BoggleApplication.PLAYER_ONE_SERVER_PORT)) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            sendSerializableRequestToPlayerOne(clientSocket, gameState);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequestToPlayerTwo(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player two!");
    }

    private static void sendSerializableRequestToPlayerOne(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player one!");
    }

    public static void sendGameState(hr.tvz.boggle.core.Board board, int currentPlayerIndex, List<Player> players, int initialTime, int roundNumber) {
        if (BoggleApplication.player.equals(PlayerType.SINGLE_PLAYER)) {
            return;
        }
        char[][] currentBoard = board.getBoard();
        String[][] stringBoard = new String[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                stringBoard[i][j] = String.valueOf(currentBoard[i][j]);
            }
        }
        GameState gameStateToSend = new GameState(stringBoard, currentPlayerIndex, players, initialTime, roundNumber);
        if (BoggleApplication.player.equals(PlayerType.PLAYER_ONE)) {
            playerOneSendRequest(gameStateToSend);
        } else if (BoggleApplication.player.equals(PlayerType.PLAYER_TWO)) {
            playerTwoSendRequest(gameStateToSend);
        }
    }
}
