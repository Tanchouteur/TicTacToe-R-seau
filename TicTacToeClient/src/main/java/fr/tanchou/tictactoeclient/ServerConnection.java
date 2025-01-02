package fr.tanchou.tictactoeclient;

import java.io.*;
import java.net.*;

public class ServerConnection {
    private final Socket socket;

    private final BufferedReader in;
    private final PrintWriter out;


    public ServerConnection(String serverAddress, int serverPort) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public String readServerMessage(){
        try {

            String message = in.readLine();
            System.out.println("Received message from server: " + message);
            return message;

        } catch (IOException e) {

            System.err.println("Error reading from server: " + e.getMessage());
            return null;

        }
    }

    public void sendToServer(String message) {
        System.out.println("Sent message to server: " + message);
        this.out.println(message);
    }

    public void close() throws IOException {
        this.socket.close();
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }
}
