package hr.tvz.boggle.boggleapplication;

import java.util.Random;

public class Board {

    // Popis kocki (sadržaj svake kocke na Boggle ploči)
    private final String[] dice = {
            "AAAFRS", "AAEEEE", "AAPSSU", "ABELRS", "ABDELX", "ACDEMP",
            "ACELRS", "ADENNN", "AEEGNN", "AEEGMU", "AEGMNN", "AFIRSY",
            "BDEOZV", "BDEORT", "BILLRY", "CDERXU"
    };

    private char[][] board;

    public Board() {
        board = new char[4][4]; // Ploču 4x4
        generateBoard();
    }

    void generateBoard() {
        Random random = new Random();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // Odaberi slučajnu kocku i slučajno slovo s te kocke
                String die = dice[random.nextInt(dice.length)];
                board[i][j] = die.charAt(random.nextInt(die.length()));
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }
}
