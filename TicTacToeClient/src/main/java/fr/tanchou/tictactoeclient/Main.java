package fr.tanchou.tictactoeclient;

import java.io.*;
import java.net.*;

import static java.lang.Thread.sleep;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "129.151.251.156"; // Adresse du serveur
        int serverPort = 8092; // Port du serveur

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connecté au serveur " + serverAddress + ":" + serverPort);

            // Lire le message de bienvenue du serveur
            String serverMessage = in.readLine();
            System.out.println("Serveur : " + serverMessage);

            // Envoyer le code d'authentification
            serverMessage = in.readLine(); // Lire "Veuillez entrer le code d'accès :"
            System.out.println("Serveur : " + serverMessage);

            String code = "1234"; // Code d'authentification
            out.println(code); // Envoyer le code

            // Lire la réponse du serveur
            serverMessage = in.readLine();
            System.out.println("Serveur : " + serverMessage);

            // Interaction avec le serveur (en attente d'une partie ou autres instructions)
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Serveur : " + serverMessage);
                if (serverMessage.contains("Votre tour")) {
                    System.out.print("Entrez votre coup (exemple : 1,1) : ");
                    String move = consoleInput.readLine();
                    out.println(move); // Envoyer le coup
                }
            }
            System.out.println("Fin de la connexion avec le serveur");
        } catch (IOException e) {
            System.err.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }
}