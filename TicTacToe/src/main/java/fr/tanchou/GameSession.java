package fr.tanchou;

import java.net.*;

public class GameSession implements Runnable {

    private final Player player1;
    private final Player player2;

    private final int[][] board = new int[3][3];
    boolean gameOver = false;

    public GameSession(Socket player1, Socket player2) {
        this.player1 = new Player(player1, 1);
        this.player2 = new Player(player2, 2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
    }

    @Override
    public void run() {
        // Initialisation de la partie
        player1.send("Partie commencée. Vous êtes le joueur 1.");
        player2.send("Partie commencée. Vous êtes le joueur 2.");

        // Logique de jeu ici

        while (!gameOver) {

            // Joueur 1
            int result;
            do {
                result = step(player1);
            }while (result == 2);


            // Joueur 2
            do {
                result = step(player2);
            }while (result == 2);
        }

        // Fin de la partie
        player1.send("Partie terminée.");
        player2.send("Partie terminée.");
    }

    private int step(Player player) {
        int[] move1 = askToPlayer(player);

        if (move1.length == 1) return 404;
        else if (move1.length == 0) return 2;

        if (!placeMove(player.number , move1[0], move1[1])){
            player.send("Coup invalide. Réessayez.");
            return 2;
        }

        // Vérifier si le joueur a gagné
        if (hasWon(player.number)) {

            player.send("Vous avez gagné.");

            player1.send(drawBoard());
            player2.send(drawBoard());

            player1.close();
            player2.close();

            gameOver = true;
            return 1;
        }

        return 0;
    }

    // Méthodes pour faire avancer le jeu.
    private int[] askToPlayer(Player player) {
        player.send(drawBoard());

        player.send("Votre tour. Entrez votre coup :");
        String move1 = player.receive();

        if (move1 == null) return new int[1]; // Vérifier si la connexion a été fermée

        try {

            int row = Integer.parseInt(move1.split(",")[0]);
            int col = Integer.parseInt(move1.split(",")[1]);

            return new int[]{row, col};

        }catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            player.send("Coup invalide. Réessayez.");
            return new int[0];
        }
    }

    private boolean placeMove(int player, int row, int col) {
        if (board[row][col] != 0) {
            return false;
        }
        board[row][col] = player;
        return true;
    }

    private String drawBoard() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            sb.append(board[i][0]).append(" ").append(board[i][1]).append(" ").append(board[i][2]).append("\n");
        }
        return sb.toString();
    }

    private boolean hasWon(int playerNumber) {
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
}