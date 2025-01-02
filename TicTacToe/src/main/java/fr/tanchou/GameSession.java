package fr.tanchou;

import java.util.LinkedList;
import java.util.List;

public class GameSession implements Runnable {

    private final List<Player> players = new LinkedList<>();

    private final Board board;

    boolean gameOver = false;

    public GameSession(Player player1, Player player2) {
        this.players.add(player1);
        this.players.add(player2);

        this.board = new Board();
    }

    @Override
    public void run() {
        // Logique de jeu ici
        for (Player player : players) {
            player.sendMessage(MessageType.GAMESTATUS, "STARTED");
        }

        while (!gameOver && players.getFirst().isConnected() && players.getLast().isConnected()) {

            // Joueur 1
            int result;
            do {
                result = step(players.getFirst());
            }while (result == 2);


            // Joueur 2
            do {
                result = step(players.getLast());
            }while (result == 2);
        }

        for (Player player : players) {
            if (player.isConnected()) {
                player.sendMessage(MessageType.GAMESTATUS, "ended");
            }
        }
    }

    private int step(Player player) {
        int[] move = askToPlayer(player);

        if (!board.placeMove(player.number , move[0], move[1])){
            player.sendMessage(MessageType.RESPONDSTATUS, "false");
            return 2;
        }

        // Envoyer le mouvement à l'autre joueur
        players.get((player.number) % 2).sendMessage(MessageType.ENEMYMOVE, move[0] + "," + move[1]);

        player.sendMessage(MessageType.RESPONDSTATUS, "true");

        // Vérifier si le joueur a gagné
        if (board.hasWon(player.number)) {

            player.sendMessage(MessageType.GAMESTATUS, "win");
            players.get((player.number+1) % 2).sendMessage(MessageType.GAMESTATUS, "lose");

            gameOver = true;
            return 1;
        }

        return 0;
    }

    // Méthodes pour faire avancer le jeu.
    private int[] askToPlayer(Player player) {

        if (!player.isConnected()) {
            this.gameOver = true;
            players.remove(player);

            for (Player p : players) {
                p.sendMessage(MessageType.GAMESTATUS, "enemydisconnected");
            }

            Thread.currentThread().interrupt();
        }

        player.sendMessage(MessageType.GAMESTATUS, "yourturn");


        String message = player.receive();

        String move = message.split(";")[1];

        try {

            int row = Integer.parseInt(move.split(",")[0]);

            int col = Integer.parseInt(move.split(",")[1]);

            return new int[]{row, col};

        }catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            return new int[]{0, 0};
        }
    }
}