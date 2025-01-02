package fr.tanchou;


public class Board {

    private final int[][] board = new int[3][3];

    public Board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
    }

    boolean placeMove(int player, int row, int col) {
        if (board[row][col] != 0) {
            return false;
        }
        board[row][col] = player;
        return true;
    }

    boolean hasWon(int playerNumber) {
        // Vérifier les lignes
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == playerNumber && board[i][1] == playerNumber && board[i][2] == playerNumber) {
                return true;
            }
        }

        // Vérifier les colonnes
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == playerNumber && board[1][i] == playerNumber && board[2][i] == playerNumber) {
                return true;
            }
        }

        // Vérifier les diagonales
        if (board[0][0] == playerNumber && board[1][1] == playerNumber && board[2][2] == playerNumber) {
            return true;
        }
        return board[0][2] == playerNumber && board[1][1] == playerNumber && board[2][0] == playerNumber;
    }

    @Override //ToString qui permet d'envoyer le plateau de jeu au client sous forme de GSON
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 3; i++) {
            sb.append("[");
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
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
}
