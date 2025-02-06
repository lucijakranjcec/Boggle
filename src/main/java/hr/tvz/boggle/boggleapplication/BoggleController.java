package hr.tvz.boggle.boggleapplication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.io.IOException;
import java.util.*;

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
    private Label wordsCorrect;

    @FXML
    private Label wordCounterLabel;

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
    private StringBuilder currentWord;
    private Label lastSelectedCell;

    private List<Player> players;
    private int currentPlayerIndex = 0;
    private int timeLeft = 10;  // 1 minute timer
    private Timeline timeline;

    private boolean gameInProgress = false;

    public void initialize() {
        try {
            dictionary = new Dictionary("src/main/resources/words.txt");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Unable to load dictionary.");
            return;
        }

        submitButton.setOnAction(e -> handleWordSubmit());
        clearButton.setOnAction(e -> handleClearSelection());
        nextPlayerButton.setOnAction(e -> handleNextPlayer());

        selectedCells = new HashSet<>();
        currentWord = new StringBuilder();

        nextPlayerBox.setVisible(false);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        updatePlayerUI();
    }

    private void updatePlayerUI() {
        Player currentPlayer = players.get(currentPlayerIndex);
        wordCounterLabel.setText(String.valueOf(currentPlayer.getWordCount()));
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
    }

    public void startGame() {
        // Initialize the board and start the game
        board = new Board();
        board.generateBoard();  // Ensure the board is populated with random letters
        gameInProgress = true;
        updateBoardUI();
        startTimer();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeLeft > 0) {
                timeLeft--;
                timerLabel.setText("Time: " + timeLeft + "s");
            } else {
                timeline.stop(); // Zaustavi timer kada dođe do 0
                endTurn(); // Pozovi kraj poteza
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void endTurn() {
        gameInProgress = false;

        if (currentPlayerIndex == players.size() - 1) {
            // Hide all UI elements and remove them from the layout (no empty space)
            boardGrid.setVisible(false);
            boardGrid.setManaged(false);  // Removes boardGrid from layout

            submitButton.setVisible(false);
            submitButton.setManaged(false);  // Removes submit button from layout

            clearButton.setVisible(false);
            clearButton.setManaged(false);  // Removes clear button from layout

            timerLabel.setVisible(false);
            timerLabel.setManaged(false);  // Removes timer label from layout

            wordsCorrect.setVisible(false);
            wordsCorrect.setManaged(false);  // Removes words label from layout

            wordCounterLabel.setVisible(false);
            wordCounterLabel.setManaged(false);  // Removes word counter label from layout

            nextPlayerBox.setVisible(false);
            nextPlayerBox.setManaged(false);  // Removes next player box from layout

            nextPlayerButton.setVisible(false);
            nextPlayerButton.setManaged(false);  // Removes next player button from layout

            // Show the results in the game over box
            gameOverBox.setVisible(true);
            gameOverBox.setManaged(true);  // Ensure game over box is visible and managed

            displayGameResults();
        } else {
            // Move to the next player
            currentPlayerIndex++;  // Increment the player index
            updatePlayerUI();  // Update the UI for the new player

            // Update the nextPlayerLabel with the next player's name
            nextPlayerLabel.setText("Next Player: " + players.get(currentPlayerIndex).getName());

            nextPlayerBox.setVisible(true);  // Make the "Next Player" box visible
            nextPlayerButton.setDisable(false);  // Enable the "Next Player" button
            gameOverBox.setVisible(false);  // Hide the game over box
        }
    }

    private void displayGameResults() {
        String winnerDetails = getWinnerDetails();
        winnerLabel.setText(winnerDetails);  // Display the winner

        displayScores();  // Update the scoreLabel with the final scores
    }

    private String getWinnerDetails() {
        Player winner = players.get(0);
        for (Player player : players) {
            if (player.getWordCount() > winner.getWordCount()) {
                winner = player;
            }
        }
        return "Winner: " + winner.getName();
    }

    private void displayScores() {
        StringBuilder scores = new StringBuilder();
        for (Player player : players) {
            scores.append(player.getName()).append(": ").append(player.getWordCount()).append(" points\n");
        }
        scoreLabel.setText("Final Scores:\n" + scores.toString());
    }

    private void resetForNextPlayer() {
        timeLeft = 10;
        timerLabel.setText("Time: " + timeLeft + "s");

        selectedCells.clear();
        currentWord.setLength(0);

        board.generateBoard(); // Generira novu ploču s novim slovima
        updateBoardUI();       // Ažurira UI s novim slovima
        updatePlayerUI();

        gameInProgress = true;
        startTimer(); // Ponovno pokreni timer
    }

    @FXML
    private void handleNextPlayer() {
        nextPlayerBox.setVisible(false); // Sakrij obavijest
        nextPlayerButton.setDisable(true); // Onemogući dugme dok se ne završi krug

        if (currentPlayerIndex < players.size() - 1) {
            resetForNextPlayer();
        }
    }

    private void updateBoardUI() {
        boardGrid.getChildren().clear();
        char[][] boardData = board.getBoard();

        // Log the board data to check if the letters are being retrieved correctly
        System.out.println("Board Data:");
        for (char[] row : boardData) {
            System.out.println(Arrays.toString(row));  // Debug output to check letters
        }

        if (boardData == null) {
            showAlert(Alert.AlertType.ERROR, "Board data is null.");
            return;
        }

        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                // Log the position and letter for each cell being added
                System.out.println("Adding cell at (" + i + ", " + j + ") with letter: " + boardData[i][j]);

                Label cell = new Label(" " + boardData[i][j] + " ");
                cell.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; -fx-alignment: center; -fx-background-color: #ecf0f1;");
                if (selectedCells.contains(cell)) {
                    cell.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; -fx-alignment: center;");
                }

                cell.setOnMouseClicked(this::handleCellClick);
                boardGrid.add(cell, j, i);

                // Log after adding each cell to the GridPane
                System.out.println("Cell added to grid at (" + i + ", " + j + ")");
            }
        }
    }


    private void handleCellClick(MouseEvent event) {
        Label cell = (Label) event.getSource();
        String currentLetter = cell.getText().trim();

        if (lastSelectedCell != null) {
            int lastRow = GridPane.getRowIndex(lastSelectedCell);
            int lastCol = GridPane.getColumnIndex(lastSelectedCell);
            int currentRow = GridPane.getRowIndex(cell);
            int currentCol = GridPane.getColumnIndex(cell);

            if (Math.abs(lastRow - currentRow) <= 1 && Math.abs(lastCol - currentCol) <= 1) {
                addCellToSelection(cell, currentLetter);
            } else {
                showAlert(Alert.AlertType.WARNING, "Please select adjacent letters.");
            }
        } else {
            addCellToSelection(cell, currentLetter);
        }
    }

    private void addCellToSelection(Label cell, String currentLetter) {
        if (selectedCells.contains(cell)) {
            selectedCells.remove(cell);
            currentWord.deleteCharAt(currentWord.length() - 1);
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; -fx-alignment: center;");
            lastSelectedCell = (selectedCells.isEmpty()) ? null : (Label) selectedCells.toArray()[selectedCells.size() - 1];
        } else {
            selectedCells.add(cell);
            currentWord.append(currentLetter);
            cell.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; -fx-alignment: center;");
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
                showAlert(Alert.AlertType.WARNING, "Already found this word!");
            } else {
                currentPlayer.incrementWordCount();
                currentPlayer.addFoundWord(word);
                wordCounterLabel.setText(String.valueOf(currentPlayer.getWordCount()));
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid word!");
        }

        currentWord.setLength(0);
        for (Label cell : selectedCells) {
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; -fx-alignment: center;");
        }
        selectedCells.clear();
        lastSelectedCell = null;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }

    @FXML
    private void handleClearSelection() {
        currentWord.setLength(0);
        for (Label cell : selectedCells) {
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; -fx-font-size: 24px; -fx-alignment: center;");
        }
        selectedCells.clear();
        lastSelectedCell = null;
    }
}
