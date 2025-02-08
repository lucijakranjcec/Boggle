package hr.tvz.boggle.ui;

import hr.tvz.boggle.core.Board;
import hr.tvz.boggle.util.DialogUtils;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Set;

public class BoardUIManager {
    private GridPane boardGrid;
    private Set<Label> selectedCells;
    private StringBuilder currentWord;
    private Label lastSelectedCell;

    public BoardUIManager(GridPane boardGrid) {
        this.boardGrid = boardGrid;
        selectedCells = new HashSet<>();
        currentWord = new StringBuilder();
    }

    public void updateBoardUI(Board board) {
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
                DialogUtils.showAlert(javafx.scene.control.Alert.AlertType.WARNING, "Please select adjacent letters.");
            }
        } else {
            addCellToSelection(cell, letter);
        }
    }

    private void addCellToSelection(Label cell, String letter) {
        if (selectedCells.contains(cell)) {
            selectedCells.remove(cell);
            if (!currentWord.isEmpty()) {
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

    public void clearSelection() {
        currentWord.setLength(0);
        for (Label cell : selectedCells) {
            cell.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: black; -fx-padding: 15px; " +
                    "-fx-font-size: 24px; -fx-alignment: center;");
        }
        selectedCells.clear();
        lastSelectedCell = null;
    }

    public String getCurrentWord() {
        return currentWord.toString();
    }
}
