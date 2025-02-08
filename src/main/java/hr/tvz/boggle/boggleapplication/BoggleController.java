package hr.tvz.boggle.boggleapplication;

import hr.tvz.boggle.core.Board;
import hr.tvz.boggle.core.Dictionary;
import hr.tvz.boggle.core.Player;
import hr.tvz.boggle.model.GameState;
import hr.tvz.boggle.model.PlayerType;
import hr.tvz.boggle.network.GameNetworkManager;
import hr.tvz.boggle.ui.BoardUIManager;
import hr.tvz.boggle.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BoggleController {

    @FXML private GridPane boardGrid;
    @FXML private Button submitButton;
    @FXML private Button clearButton;
    @FXML private Label timerLabel;
    @FXML private Label wordCounterLabel;
    @FXML private Label wordsCorrect;
    @FXML private VBox gameOverBox;
    @FXML private Label winnerLabel;
    @FXML private Label scoreLabel;
    @FXML private Label currentPlayerLabel;

    private Board board;
    private Dictionary dictionary;
    private List<Player> players;
    private int currentPlayerIndex = 0;
    private GameTimer gameTimer;
    private static final int INITIAL_TIME = 20;
    private int roundNumber = 0;
    private final int maxRounds = 3; // Adjust as needed.

    // Helper for board UI management.
    private BoardUIManager boardUIManager;

    // Static instance for network updates.
    @Getter
    private static BoggleController instance;
    public BoggleController() { instance = this; }

    public static BoggleController getInstance() {
        return instance;
    }

    public void initialize() {
        try {
            dictionary = new Dictionary("src/main/resources/words.txt");
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Unable to load dictionary.");
            return;
        }
        submitButton.setOnAction(e -> handleWordSubmit());
        clearButton.setOnAction(e -> handleClearSelection());

        boardUIManager = new BoardUIManager(boardGrid);

        Platform.runLater(this::handleNewGame);
    }

    private void updatePlayerUI() {
        Player currentPlayer = players.get(currentPlayerIndex);
        wordCounterLabel.setText(String.valueOf(currentPlayer.getWordCount()));
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
    }

    private void startTimer() {
        gameTimer = new GameTimer(INITIAL_TIME, new GameTimer.TimerListener() {
            @Override public void onTick(int secondsLeft) {
                timerLabel.setText("Time: " + secondsLeft + "s");
            }
            @Override public void onTimeEnd() { endTurn(); }
        });
        gameTimer.start();
    }

    private void endTurn() {
        if (gameTimer != null) { gameTimer.stop(); }

        if (BoggleApplication.player.equals(PlayerType.SINGLE_PLAYER)) {
            roundNumber++;
            if (roundNumber >= maxRounds) {
                showGameOverScreen();
                return;
            } else {
                resetForNextPlayer();
                return;
            }
        }

        int nextIndex = (currentPlayerIndex + 1) % players.size();
        if (nextIndex == 0) {
            roundNumber++;
            if (roundNumber >= maxRounds) {
                currentPlayerIndex = nextIndex;
                sendGameState();
                showGameOverScreen();
                return;
            }
        }
        currentPlayerIndex = nextIndex;
        sendGameState();
        disableUI();
        timerLabel.setText("Time: 0s");
    }

    private void disableUI() {
        boardGrid.setDisable(true);
        submitButton.setDisable(true);
        clearButton.setDisable(true);
    }

    private void enableUI() {
        boardGrid.setDisable(false);
        submitButton.setDisable(false);
        clearButton.setDisable(false);
    }

    private void showGameOverScreen() {
        boardGrid.setVisible(false); boardGrid.setManaged(false);
        submitButton.setVisible(false); submitButton.setManaged(false);
        clearButton.setVisible(false); clearButton.setManaged(false);
        timerLabel.setVisible(false); timerLabel.setManaged(false);
        wordCounterLabel.setVisible(false); wordCounterLabel.setManaged(false);
        wordsCorrect.setVisible(false); wordsCorrect.setManaged(false);

        gameOverBox.setVisible(true); gameOverBox.setManaged(true);
        displayGameResults();
    }

    private void displayGameResults() {
        winnerLabel.setText(GameResultsHelper.getWinnerDetails(players));
        scoreLabel.setText(GameResultsHelper.getScores(players));
    }

    private void sendGameState() {
        if (BoggleApplication.player.equals(PlayerType.SINGLE_PLAYER)) return;
        char[][] currentBoard = board.getBoard();
        String[][] stringBoard = new String[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                stringBoard[i][j] = String.valueOf(currentBoard[i][j]);
            }
        }
        GameState gameStateToSend = new GameState(stringBoard, currentPlayerIndex, players, INITIAL_TIME, roundNumber);
        GameNetworkManager.sendGameState(gameStateToSend);
    }

    public static void updateGameStateFromNetwork(GameState state) {
        BoggleController controller = getInstance();
        if (controller == null) return;

        controller.currentPlayerIndex = state.getCurrentPlayerIndex();
        controller.players = state.getPlayers();
        controller.roundNumber = state.getRoundNumber();

        if (controller.roundNumber >= controller.maxRounds) {
            Platform.runLater(controller::showGameOverScreen);
            return;
        }

        if ((BoggleApplication.player.equals(PlayerType.PLAYER_ONE) && controller.currentPlayerIndex == 0) ||
                (BoggleApplication.player.equals(PlayerType.PLAYER_TWO) && controller.currentPlayerIndex == 1)) {
            controller.board.generateBoard();
            controller.boardUIManager.updateBoardUI(controller.board);
            controller.boardGrid.setVisible(true);
            controller.timerLabel.setText("Time: " + INITIAL_TIME + "s");
            if (controller.gameTimer != null) { controller.gameTimer.stop(); }
            controller.gameTimer = new GameTimer(INITIAL_TIME, new GameTimer.TimerListener() {
                @Override public void onTick(int secondsLeft) {
                    controller.timerLabel.setText("Time: " + secondsLeft + "s");
                }
                @Override public void onTimeEnd() { controller.endTurn(); }
            });
            controller.gameTimer.start();
            controller.enableUI();
        } else {
            if (controller.gameTimer != null) {
                controller.gameTimer.stop();
                controller.gameTimer = null;
            }
            controller.timerLabel.setText("Time: 0s");
            controller.disableUI();
            controller.boardGrid.setVisible(false);
        }
        controller.updatePlayerUI();
    }

    @FXML
    private void handleNewGame() {
        if (gameTimer != null) { gameTimer.stop(); }
        roundNumber = 0;
        if (BoggleApplication.player.equals(PlayerType.SINGLE_PLAYER)) {
            players = new ArrayList<>();
            players.add(new Player("single_player"));
            currentPlayerIndex = 0;
            enableUI();
        } else {
            players = new ArrayList<>();
            players.add(new Player("player_one"));
            players.add(new Player("player_two"));
            if (BoggleApplication.player.equals(PlayerType.PLAYER_ONE)) {
                currentPlayerIndex = 0;
                enableUI();
            } else {
                currentPlayerIndex = 1;
                disableUI();
            }
        }
        updatePlayerUI();
        board = new Board();
        boardUIManager.updateBoardUI(board);
        startTimer();
    }

    private void resetForNextPlayer() {
        if (gameTimer != null) { gameTimer.stop(); gameTimer.reset(INITIAL_TIME); }
        timerLabel.setText("Time: " + INITIAL_TIME + "s");
        boardUIManager.clearSelection();
        board.generateBoard();
        boardUIManager.updateBoardUI(board);
        updatePlayerUI();
        startTimer();
    }

    @FXML
    private void handleWordSubmit() {
        String word = boardUIManager.getCurrentWord().toUpperCase();
        Player currentPlayer = players.get(currentPlayerIndex);
        if (dictionary.isValidWord(word)) {
            if (currentPlayer.hasFoundWord(word)) {
                DialogUtils.showAlert(Alert.AlertType.WARNING, "Already found this word!");
            } else {
                currentPlayer.incrementWordCount();
                currentPlayer.addFoundWord(word);
                wordCounterLabel.setText(String.valueOf(currentPlayer.getWordCount()));
            }
        } else {
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Invalid word!");
        }
        boardUIManager.clearSelection();
    }

    @FXML
    private void handleClearSelection() { boardUIManager.clearSelection(); }

    public void saveGame() {
        if (gameTimer != null) { gameTimer.stop(); }
        char[][] currentBoard = board.getBoard();
        String[][] stringBoard = new String[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                stringBoard[i][j] = String.valueOf(currentBoard[i][j]);
            }
        }
        GameState gameStateToSave = new GameState(stringBoard, currentPlayerIndex, players,
                gameTimer != null ? gameTimer.getTimeLeft() : INITIAL_TIME, roundNumber);
        GamePersistenceManager.saveGame(gameStateToSave);
        if (gameTimer != null) { gameTimer.start(); }
    }

    public void loadGame() {
        GameState loadedGame = GamePersistenceManager.loadGame();
        if (loadedGame == null) return;
        char[][] loadedBoard = new char[loadedGame.getBoardState().length][loadedGame.getBoardState()[0].length];
        for (int i = 0; i < loadedGame.getBoardState().length; i++) {
            for (int j = 0; j < loadedGame.getBoardState()[i].length; j++) {
                loadedBoard[i][j] = loadedGame.getBoardState()[i][j].charAt(0);
            }
        }
        if (board == null) { board = new Board(); }
        board.setBoard(loadedBoard);
        currentPlayerIndex = loadedGame.getCurrentPlayerIndex();
        players = loadedGame.getPlayers();
        roundNumber = loadedGame.getRoundNumber();
        int remainingTime = loadedGame.getRemainingTime();
        boardUIManager.updateBoardUI(board);
        updatePlayerUI();
        if (gameTimer != null) { gameTimer.stop(); }
        gameTimer = new GameTimer(remainingTime, new GameTimer.TimerListener() {
            @Override public void onTick(int secondsLeft) { timerLabel.setText("Time: " + secondsLeft + "s"); }
            @Override public void onTimeEnd() { endTurn(); }
        });
        gameTimer.start();
        DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Game was successfully loaded!");
    }

    public void generateHTMLDocumentation() {
        DocumentationUtils.generateDocumentation();
        DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Documentation was successfully generated!");
    }
}
