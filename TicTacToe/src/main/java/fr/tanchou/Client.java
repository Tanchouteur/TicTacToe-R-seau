package fr.tanchou;

import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket clientSocket;

    private BufferedReader in;
    private PrintWriter out;

    private boolean authenticated = false;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);

        }catch (IOException e) {
            System.out.println("Client have troubleshooting : " + e.getMessage());
        }
    }

    public void sendMessage(MessageType type, String message) {
        if (type == null){
            throw new IllegalArgumentException("Message Type is null");
        }
        System.out.println("server is sending " + type + " message : " + message);
        this.out.println(type + ";" + message);
    }

    public String getMessage() {
        try {
            System.out.println("server is waiting for a message");
            return in.readLine();
        } catch (IOException e) {
            System.out.println("Client have troubleshooting : " + e.getMessage());
            this.close();
            return null;
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Client have troubleshooting : " + e.getMessage());
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isClosed() {
        return clientSocket.isClosed();
    }

    public boolean isConnected() {
        return clientSocket.isConnected() && !clientSocket.isClosed() && !clientSocket.isInputShutdown() && !clientSocket.isOutputShutdown();
    }
}
