package fr.tanchou;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private final Socket playerSocket;
    int number;

    private BufferedReader in;
    private PrintWriter out;

    public Player(Socket playerSocket, int number) {
        this.playerSocket = playerSocket;
        this.number = number;

        try {
            this.in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            this.out = new PrintWriter(playerSocket.getOutputStream(), true);
        }catch (IOException e) {
            System.out.println("Player " + number + " have troubleshooting : " + e.getMessage());
        }

    }

    public void send(String message) {
        this.out.println(message);
    }

    public String receive(){
        try {
            return in.readLine();
        }catch (IOException e) {
            System.out.println("Player " + number + "disconnected");
            return null;
        }
    }

    public void close(){
        try {
            playerSocket.close();
        }catch (IOException e) {
            System.out.println("Player " + number + " disconnected");
        }

    }
}
