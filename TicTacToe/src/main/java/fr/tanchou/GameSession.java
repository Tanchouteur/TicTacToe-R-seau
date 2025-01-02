package fr.tanchou;

import java.net.*;

public class GameSession implements Runnable {

    private final Player player1;
    private final Player player2;

    private final Player[] players = new Player[2];

    private final Board board;

    boolean gameOver = false;

    public GameSession(Socket player1, Socket player2) {
        this.player1 = new Player(player1, 1);
        this.player2 = new Player(player2, 2);

        players[0] = this.player1;
        players[1] = this.player2;

        this.board = new Board();
    }

    @Override
    public void run() {
        // Initialisation de la partie
        player1.sendMessage("Partie commencée. Vous êtes le joueur 1.");
        player2.sendMessage("Partie commencée. Vous êtes le joueur 2.");

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
        player1.sendMessage("Partie terminée.");
        player2.sendMessage("Partie terminée.");
    }

    private int step(Player player) {
        int[] move1 = askToPlayer(player);

        if (move1.length == 1) return 404;
        else if (move1.length == 0) return 2;

        if (!board.placeMove(player.number , move1[0], move1[1])){
            player.sendMessage("Coup invalide. Réessayez.");
            return 2;
        }else {
            for (Player p : players) {
                p.sendData(board.toString());
            }
        }

        // Vérifier si le joueur a gagné
        if (board.hasWon(player.number)) {

            player.sendMessage("Vous avez gagné.");

            player1.close();
            player2.close();

            gameOver = true;
            return 1;
        }

        return 0;
    }

    // Méthodes pour faire avancer le jeu.
    private int[] askToPlayer(Player player) {

        player.sendMessage("Votre tour. Entrez votre coup :");
        String move1 = player.receive();

        if (move1 == null) return new int[1]; // Vérifier si la connexion a été fermée

        try {

            int row = Integer.parseInt(move1.split(",")[0]);
            int col = Integer.parseInt(move1.split(",")[1]);

            return new int[]{row, col};

        }catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            player.sendMessage("Coup invalide. Réessayez.");
            return new int[0];
        }
    }
}