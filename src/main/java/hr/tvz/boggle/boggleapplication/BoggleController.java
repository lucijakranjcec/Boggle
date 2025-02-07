package hr.tvz.boggle.boggleapplication;

import hr.tvz.boggle.model.GameState;
import hr.tvz.boggle.util.DialogUtils;
import hr.tvz.boggle.util.GameTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleController {

    @FXML
    private GridPane boardGrid;

    @FXML
    private Button submitButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label timerLabel;

    @FXML
    private Label wordCounterLabel;

    @FXML
    private Label wordsCorrect;

    @FXML
    private VBox nextPlayerBox;

    @FXML
    private Label nextPlayerLabel;

    @FXML
    private Button nextPlayerButton;

    @FXML
    private VBox gameOverBox;

    @FXML
    private Label winnerLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label currentPlayerLabel;

    private Board board;
    private Dictionary dictionary;

    private Set<Label> selectedCells;

    // Builds the current word from selected letters
    private StringBuilder currentWord;

    private Label lastSelectedCell;

    private List<Player> players;
    private int currentPlayerIndex = 0;

    // Use GameTimer for timer logic; initial time is 10 seconds.
    private GameTimer gameTimer;
    private static final int INITIAL_TIME = 20;

    // Called automatically after the FXML loads
    public void initialize() {
        try {
            dictionary = new Dictionary("src/main/resources/words.txt");
        } catch (IOException e) {
            e.printStackTrace();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Unable to load dictionary.");
            return;
        }
        submitButton.setOnAction(e -> handleWordSubmit());
        clearButton.setOnAction(e -> handleClearSelection());
        nextPlayerButton.setOnAction(e -> handleNextPlayer());
        selectedCells = new HashSet<>();
        currentWord = new StringBuilder();
        nextPlayerBox.setVisible(false);
    }

    // Called from the player setup screen to pass in the players
    public void setPlayers(List<Player> players) {
        this.players = players;
        updatePlayerUI();
    }

    // Update the UI to show the current player's info
    private void updatePlayerUI() {
        Player currentPlayer = players.get(currentPlayerIndex);
        wordCounterLabel.setText(String.valueOf(currentPlayer.getWordCount()));
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
    }

    // Start the game by setting up the board and timer
    public void startGame() {
        board = new Board(); // The constructor already generates the board
        updateBoardUI();
        startTimer();
    }

    // Initialize and start the GameTimer
    private void startTimer() {
        gameTimer = new GameTimer(INITIAL_TIME, new GameTimer.TimerListener() {
            @Override
            public void onTick(int secondsLeft) {
                timerLabel.setText("Time: " + secondsLeft + "s");
            }
            @Override
            public void onTimeEnd() {
                endTurn();
            }
        });
        gameTimer.start();
    }

    private void endTurn() {
        if (currentPlayerIndex == players.size() - 1) {
            // Game over: hide game elements and show final results
            boardGrid.setVisible(false);
            boardGrid.setManaged(false);
            submitButton.setVisible(false);
            submitButton.setManaged(false);
            clearButton.setVisible(false);
            clearButton.setManaged(false);
            timerLabel.setVisible(false);
            timerLabel.setManaged(false);
            wordCounterLabel.setVisible(false);
            wordCounterLabel.setManaged(false);
            wordsCorrect.setVisible(false);
            wordsCorrect.setManaged(false);
            nextPlayerBox.setVisible(false);
            nextPlayerBox.setManaged(false);
            nextPlayerButton.setVisible(false);
            nextPlayerButton.setManaged(false);

            // Show game over box with results
            gameOverBox.setVisible(true);
            gameOverBox.setManaged(true);
            displayGameResults();
        } else {
            // Switch to the next player
            currentPlayerIndex++;
            updatePlayerUI();
            nextPlayerLabel.setText("Next Player: " + players.get(currentPlayerIndex).getName());
            nextPlayerBox.setVisible(true);
            nextPlayerButton.setDisable(false);
            gameOverBox.setVisible(false);
        }
    }

    private void displayGameResults() {
        winnerLabel.setText(getWinnerDetails());
        scoreLabel.setText(getScores());
    }

    private String getWinnerDetails() {
        int highestScore = 0;
        for (Player player : players) {
            if (player.getWordCount() > highestScore) {
                highestScore = player.getWordCount();
            }
        }
        int count = 0;
        for (Player player : players) {
            if (player.getWordCount() == highestScore) {
                count++;
            }
        }
        if (count > 1) {
            return "Winner: It's a draw!";
        } else {
            for (Player player : players) {
                if (player.getWordCount() == highestScore) {
                    return "Winner: " + player.getName();
                }
            }
        }
        return "Winner: Unknown";
    }

    // Create a string with all players' scores
    private String getScores() {
        StringBuilder scores = new StringBuilder();
        for (Player player : players) {
            scores.append(player.getName())
                    .append(": ")
                    .append(player.getWordCount())
                    .append(" points\n");
        }
        return "Final Scores:\n" + scores;
    }

    private void resetForNextPlayer() {
        // Reset the timer
        if (gameTimer != null) {
            gameTimer.stop();
            gameTimer.reset(INITIAL_TIME);
        }
        timerLabel.setText("Time: " + INITIAL_TIME + "s");
        selectedCells.clear();
        currentWord.setLength(0);
        board.generateBoard(); // Get new letters for the board
        updateBoardUI();
        updatePlayerUI();
        startTimer();
    }

    @FXML
    private void handleNextPlayer() {
        nextPlayerBox.setVisible(false);
        nextPlayerButton.setDisable(true);
        resetForNextPlayer();
    }

    @FXML
    private void handleNewGame() {
        try {
            // Load the player setup screen FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/boggle/boggleapplication/player_setup.fxml"));
            VBox root = loader.load();

            // Create a new scene with the player setup UI
            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/hr/tvz/boggle/boggleapplication/style.css").toExternalForm());

            // Get the current stage using one of UI elements and set the new scene
            Stage stage = (Stage) currentPlayerLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Error loading player setup screen.");
        }
    }


    // Update the board UI with the current letters
    private void updateBoardUI() {
        boardGrid.getChildren().clear();
        char[][] boardData = board.getBoard();
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                Label cell = new Label(" " + boardData[i][j] + " ");
                cell.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; " +
                        "-fx-alignment: center; -fx-background-color: #ecf0f1;");
                cell.setOnMouseClicked(this::handleCellClick);
                boardGrid.add(cell, j, i);
            }
        }
    }

    private void handleCellClick(MouseEvent event) {
        Label cell = (Label) event.getSource();
        String letter = cell.getText().trim();
        if (lastSelectedCell != null) {
            int lastRow = GridPane.getRowIndex(lastSelectedCell);
            int lastCol = GridPane.getColumnIndex(lastSelectedCell);
            int currentRow = GridPane.getRowIndex(cell);
            int currentCol = GridPane.getColumnIndex(cell);
            if (Math.abs(lastRow - currentRow) <= 1 && Math.abs(lastCol - currentCol) <= 1) {
                addCellToSelection(cell, letter);
            } else {
                DialogUtils.showAlert(Alert.AlertType.WARNING, "Please select adjacent letters.");
            }
        } else {
            addCellToSelection(cell, letter);
        }
    }

    private void addCellToSelection(Label cell, String letter) {
        if (selectedCells.contains(cell)) {
            selectedCells.remove(cell);
            if (currentWord.length() > 0) {
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; " +
                    "-fx-font-size: 24px; -fx-alignment: center;");
            lastSelectedCell = selectedCells.isEmpty() ? null : (Label) selectedCells.toArray()[selectedCells.size() - 1];
        } else {
            selectedCells.add(cell);
            currentWord.append(letter);
            cell.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-padding: 15px; " +
                    "-fx-font-size: 24px; -fx-alignment: center;");
            lastSelectedCell = cell;
        }
        System.out.println("Current word: " + currentWord.toString());
    }

    @FXML
    private void handleWordSubmit() {
        String word = currentWord.toString().toUpperCase();
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
        clearSelection();
    }

    @FXML
    private void handleClearSelection() {
        clearSelection();
    }

    // Helper method to clear the current cell selection and word
    private void clearSelection() {
        currentWord.setLength(0);
        for (Label cell : selectedCells) {
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; " +
                    "-fx-font-size: 24px; -fx-alignment: center;");
        }
        selectedCells.clear();
        lastSelectedCell = null;
    }

    public void saveGame() {
        // Stop the timer when saving the game
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Convert the board's char[][] into a String[][] so that it can be saved
        char[][] currentBoard = board.getBoard();
        String[][] stringBoard = new String[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                stringBoard[i][j] = String.valueOf(currentBoard[i][j]);
            }
        }

        // Get the remaining time from your GameTimer
        int remainingTime = gameTimer != null ? gameTimer.getTimeLeft() : INITIAL_TIME;

        // Create a GameState object with all necessary info
        GameState gameStateToSave = new GameState(stringBoard, currentPlayerIndex, players, remainingTime);

        // Save the game state to a file using serialization
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saveGame/gameSave.dat"))) {
            oos.writeObject(gameStateToSave);

            // Create a modal confirmation dialog that blocks until closed
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Game");
            alert.setHeaderText(null);
            alert.setContentText("Game was successfully saved!");
            alert.showAndWait(); // Wait until the user closes the dialog
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Resume the timer after the dialog is dismissed
        if (gameTimer != null) {
            gameTimer.start();
        }
    }

    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saveGame/gameSave.dat"))) {
            // Read the saved GameState object
            GameState loadedGame = (GameState) ois.readObject();

            // Convert the loaded String[][] back into a char[][] board state
            String[][] stringBoard = loadedGame.getBoardState();
            char[][] loadedBoard = new char[stringBoard.length][stringBoard[0].length];
            for (int i = 0; i < stringBoard.length; i++) {
                for (int j = 0; j < stringBoard[i].length; j++) {
                    loadedBoard[i][j] = stringBoard[i][j].charAt(0);
                }
            }

            // If board is null, initialize it.
            if (board == null) {
                board = new Board();
            }
            board.setBoard(loadedBoard);  // Now board is not null

            currentPlayerIndex = loadedGame.getCurrentPlayerIndex();
            players = loadedGame.getPlayers();
            int remainingTime = loadedGame.getRemainingTime();

            // Refresh the UI with the loaded state
            updateBoardUI();
            updatePlayerUI();

            // Restart the timer with the loaded remaining time
            if (gameTimer != null) {
                gameTimer.stop();
            }
            gameTimer = new GameTimer(remainingTime, new GameTimer.TimerListener() {
                @Override
                public void onTick(int secondsLeft) {
                    timerLabel.setText("Time: " + secondsLeft + "s");
                }
                @Override
                public void onTimeEnd() {
                    endTurn();
                }
            });
            gameTimer.start();

            DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Game was successfully loaded!");
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
