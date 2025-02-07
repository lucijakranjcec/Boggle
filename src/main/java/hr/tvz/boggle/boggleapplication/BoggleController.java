package hr.tvz.boggle.boggleapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    private static final int INITIAL_TIME = 10;

    // Called automatically after the FXML loads
    public void initialize() {
        try {
            dictionary = new Dictionary("src/main/resources/words.txt");
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Unable to load dictionary.");
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
        // Find the highest score
        for (Player player : players) {
            if (player.getWordCount() > highestScore) {
                highestScore = player.getWordCount();
            }
        }
        int count = 0;
        // Count how many players have that highest score
        for (Player player : players) {
            if (player.getWordCount() == highestScore) {
                count++;
            }
        }
        if (count > 1) {
            return "Winner: It's a draw!";
        } else {
            // Return the name of the player with the highest score
            for (Player player : players) {
                if (player.getWordCount() == highestScore) {
                    return "Winner: " + player.getName();
                }
            }
        }
        return "Winner: Unknown"; // Fallback (should not occur)
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

    // Update the board UI with the current letters
    private void updateBoardUI() {
        boardGrid.getChildren().clear();
        char[][] boardData = board.getBoard();
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                // Create a cell label for each letter
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

        // If there is already a cell selected, check if the new cell is adjacent
        if (lastSelectedCell != null) {
            int lastRow = GridPane.getRowIndex(lastSelectedCell);
            int lastCol = GridPane.getColumnIndex(lastSelectedCell);
            int currentRow = GridPane.getRowIndex(cell);
            int currentCol = GridPane.getColumnIndex(cell);
            if (Math.abs(lastRow - currentRow) <= 1 && Math.abs(lastCol - currentCol) <= 1) {
                addCellToSelection(cell, letter);
            } else {
                AlertHelper.showAlert(Alert.AlertType.WARNING, "Please select adjacent letters.");
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
            // Reset cell style to default
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; " +
                    "-fx-font-size: 24px; -fx-alignment: center;");
            lastSelectedCell = selectedCells.isEmpty() ? null : (Label) selectedCells.toArray()[selectedCells.size() - 1];
        } else {
            selectedCells.add(cell);
            currentWord.append(letter);
            // Change cell style to indicate selection
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
                AlertHelper.showAlert(Alert.AlertType.WARNING, "Already found this word!");
            } else {
                currentPlayer.incrementWordCount();
                currentPlayer.addFoundWord(word);
                wordCounterLabel.setText(String.valueOf(currentPlayer.getWordCount()));
            }
        } else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Invalid word!");
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
}
