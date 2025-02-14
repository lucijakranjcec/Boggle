package hr.tvz.boggle.core;

import java.util.Random;

public class Board {

    // Each string represents the letters on one die
    private final String[] dice = {
            "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
            "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
            "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
            "EIOSST", "ELRTTY", "HIMNUQ", "HLNNRZ"
    };

    private char[][] board;

    public Board() {
        board = new char[4][4];
        generateBoard();
    }

    public void generateBoard() {
        Random random = new Random();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                String die = dice[random.nextInt(dice.length)];
                board[i][j] = die.charAt(random.nextInt(die.length()));
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] newBoard) {
        this.board = newBoard;
    }
}
