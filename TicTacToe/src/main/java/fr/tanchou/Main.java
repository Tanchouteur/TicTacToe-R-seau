package fr.tanchou;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8092;
        System.out.println("Démarrage du serveur TicTacToe sur le port " + port);

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
            System.out.println("Serveur à l'écoute sur 0.0.0.0:" + port);

            Lobby lobby = new Lobby();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> lobby.handleNewConnection(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du serveur : " + e.getMessage());
        }
    }
}