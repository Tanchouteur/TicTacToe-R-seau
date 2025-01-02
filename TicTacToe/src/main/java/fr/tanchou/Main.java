package fr.tanchou;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        int port = 8092; // Port d'écoute

        try (ServerSocket serverSocket = new ServerSocket()) {

            serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
            System.out.println("Serveur à l'écoute sur 0.0.0.0:" + port);

            while (true) {
                // Accepter les connexions entrantes
                serverSocket.accept();
                System.out.println("Nouvelle connexion !");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du serveur : " + e.getMessage());
        }
    }
}