package fr.tanchou;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Lobby {
    private static final String AUTH_CODE = "1234"; // Code secret pour se connecter

    private final BlockingQueue<Client> playerQueue = new LinkedBlockingQueue<>();

    private final ScheduledThreadPoolExecutor gameSessionsExecutor = new ScheduledThreadPoolExecutor(10);

    private final List<GameSession> gameSessions = new ArrayList<>();

    public void handleNewConnection(Socket clientSocket) {

        try {
            Client client = new Client(clientSocket);

            System.out.println("Nouvelle connexion : " + clientSocket.getInetAddress().getHostAddress());

            client.sendMessage(MessageType.PASSWORD, "get");

            String clientCode = client.getMessage();

            if (AUTH_CODE.equals(clientCode)) {
                client.setAuthenticated(true);
                client.sendMessage(MessageType.RESPONDSTATUS, "ok");
            }

            if (!client.isAuthenticated()) {
                client.sendMessage(MessageType.PASSWORD, "err");
                return;
            }

            playerQueue.put(client);
            System.out.println("Joueur ajouté à la file d'attente. Taille de la file : " + playerQueue.size());

            client.sendMessage(MessageType.GAMESTATUS, "WAITING");

            // Attendre jusqu'à ce que deux joueurs soient prêts
            while (true) {

                Player player1 = new Player(playerQueue.take(), 1);
                Player player2 = new Player(playerQueue.take(), 2);

                // Vérifier si l'un des joueurs est déconnecté avant de démarrer
                if (!player1.isConnected() || !player2.isConnected()) {
                    System.out.println("Un joueur s'est déconnecté avant le début de la partie.");
                    if (player1.isConnected()){
                        playerQueue.put(player1.getClient());
                    }else {
                        playerQueue.put(player2.getClient());
                    }
                    continue;
                }

                System.out.println("Deux joueurs trouvés, démarrage de la partie !");

                GameSession gameSession = new GameSession(player1, player2);
                this.gameSessions.add(gameSession);
                gameSessionsExecutor.execute(gameSession);
            }


        } catch (InterruptedException e) {

            System.out.println("Erreur lors de la gestion de la connexion : " + e.getMessage());

        }
    }
}