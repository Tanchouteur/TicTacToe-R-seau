package fr.tanchou;

import java.io.*;
import java.net.*;

public class GameSession implements Runnable {
    private final Socket player1;
    private final Socket player2;

    public GameSession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try (BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
             PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
             BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
             PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true)) {

            out1.println("La partie commence ! Vous êtes le joueur 1.");
            out2.println("La partie commence ! Vous êtes le joueur 2.");

            boolean gameRunning = true;
            while (gameRunning) {
                // Exemple : recevoir un coup de joueur 1
                out1.println("Votre tour !");
                String move1 = in1.readLine();
                if (move1 == null) break; // Déconnexion

                out2.println("Joueur 1 a joué : " + move1);

                // Recevoir un coup de joueur 2
                out2.println("Votre tour !");
                String move2 = in2.readLine();
                if (move2 == null) break; // Déconnexion

                out1.println("Joueur 2 a joué : " + move2);
            }
        } catch (IOException e) {
            System.out.println("Erreur pendant la partie : " + e.getMessage());
        } finally {
            try {
                player1.close();
                player2.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture des connexions : " + e.getMessage());
            }
        }
    }
}
