package fr.tanchou;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Lobby {
    private static final String AUTH_CODE = "1234"; // Code secret pour se connecter
    private final BlockingQueue<Socket> playerQueue = new LinkedBlockingQueue<>();
    private final ScheduledThreadPoolExecutor gameSessions = new ScheduledThreadPoolExecutor(10);

    public void handleNewConnection(Socket clientSocket) {
        BufferedReader in = null;
        PrintWriter clientOut = null;

        try {
            // Création des flux d'entrée et sortie manuellement
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

            clientOut.println("Bienvenue sur le serveur TicTacToe !");
            clientOut.println("Veuillez entrer le code d'accès :");

            String clientCode = in.readLine();
            if (AUTH_CODE.equals(clientCode)) {
                clientOut.println("Code accepté.");
                // Lancer un thread dédié pour gérer la file d'attente et l'attente des adversaires
                new Thread(new PlayerWaiter(clientSocket, clientOut)).start();
            } else {
                clientOut.println("Code incorrect. Déconnexion.");
                clientSocket.close();  // Déconnexion si code incorrect
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la gestion de la connexion : " + e.getMessage());
        } finally {
            // Ne pas fermer le clientSocket ici pour laisser la gestion ouverte dans PlayerWaiter
            // Le socket sera fermé dans GameSession ou si la connexion est coupée avant la partie
        }
    }

    // Classe qui s'occupe de la gestion de l'attente d'un adversaire pour chaque joueur
    private class PlayerWaiter implements Runnable {
        private final Socket clientSocket;
        private final PrintWriter clientOut;

        public PlayerWaiter(Socket clientSocket, PrintWriter clientOut) {
            this.clientSocket = clientSocket;
            this.clientOut = clientOut;
        }

        @Override
        public void run() {
            try {
                // Ajouter le joueur à la file d'attente
                playerQueue.put(clientSocket);
                System.out.println("Joueur ajouté à la file d'attente. Taille de la file : " + playerQueue.size());
                clientOut.println("En attente d'un adversaire...");

                // Attendre jusqu'à ce que deux joueurs soient prêts
                while (true) {
                        // Deux joueurs sont prêts à commencer la partie
                        Socket player1 = playerQueue.take();
                        Socket player2 = playerQueue.take();

                        // Vérifier si l'un des joueurs est déconnecté avant de démarrer
                        if (player1.isClosed() || player2.isClosed()) {
                            System.out.println("Un joueur s'est déconnecté avant le début de la partie.");
                            continue;  // Passer à la prochaine itération, en attendant de nouveaux joueurs
                        }

                        System.out.println("Deux joueurs trouvés, démarrage de la partie !");
                        // Démarrer une session de jeu dans un nouveau thread
                        gameSessions.execute(new GameSession(player1, player2));
                        break;  // Sortir de la boucle une fois que la partie est lancée
                }
            } catch (InterruptedException e) {
                System.out.println("Erreur lors de l'attente du joueur : " + e.getMessage());
                Thread.currentThread().interrupt();  // Restaurer l'état d'interruption
            }
        }
    }
}