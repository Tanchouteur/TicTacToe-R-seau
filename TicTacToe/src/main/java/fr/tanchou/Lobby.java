package fr.tanchou;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Lobby {
    private static final String AUTH_CODE = "1234"; // Code secret pour se connecter
    private final BlockingQueue<Socket> playerQueue = new LinkedBlockingQueue<>();

    public void handleNewConnection(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Bienvenue sur le serveur TicTacToe !");
            out.println("Veuillez entrer le code d'accès :");

            String clientCode = in.readLine();
            if (AUTH_CODE.equals(clientCode)) {
                out.println("Code accepté. Vous êtes en attente d'un adversaire...");
                addToQueue(clientSocket);
            } else {
                out.println("Code incorrect. Déconnexion.");
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la gestion de la connexion : " + e.getMessage());
        }
    }

    private void addToQueue(Socket clientSocket) {
        try {
            playerQueue.put(clientSocket); // Ajouter le joueur à la file
            if (playerQueue.size() % 2 == 0) {
                // Deux joueurs disponibles, créer une nouvelle partie
                Socket player1 = playerQueue.take();
                Socket player2 = playerQueue.take();
                new Thread(new GameSession(player1, player2)).start();
            }
        } catch (InterruptedException e) {
            System.out.println("Erreur lors de l'ajout d'un joueur à la file : " + e.getMessage());
        }
    }
}