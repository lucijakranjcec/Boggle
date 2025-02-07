package hr.tvz.boggle.boggleapplication;

import java.util.Random;

public class Board {

    // Each string represents the letters on one die
    private final String[] dice = {
            "AAAFRS", "AAEEEE", "AAPSSU", "ABELRS",
            "ABDELX", "ACDEMP", "ACELRS", "ADENNN",
            "AEEGNN", "AEEGMU", "AEGMNN", "AFIRSY",
            "BDEOZV", "BDEORT", "BILLRY", "CDERXU"
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
