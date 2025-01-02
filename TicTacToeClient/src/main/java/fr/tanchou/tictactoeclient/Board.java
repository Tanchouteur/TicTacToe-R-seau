package fr.tanchou.tictactoeclient;

public class Board {
    private final int[][] board = new int[3][3];

    public Board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = 0;
            }
        }
    }

    void placeMove(int row, int col, int player) {
        this.board[row][col] = player;
    }

    @Override //ToString qui permet d'envoyer le plateau de jeu au client sous forme de GSON
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 3; i++) {
            sb.append("[");
            for (int j = 0; j < 3; j++) {
                sb.append(this.board[i][j]);
                if (j < 2) {
                    sb.append(",");
                }
            }
            sb.append("]");
            if (i < 2) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public int[][] getBoard() {
        return this.board;
    }
}
