package fr.tanchou;

public class Player {
    int number;
    Client client;

    public Player(Client client, int number) {
        this.number = number;
        this.client = client;
    }

    public void sendMessage(MessageType type, String message) {
        client.sendMessage(type, message);
    }

    public String receive(){
        return client.getMessage();
    }

    public void close(){
        client.close();
    }

    public boolean isClosed() {
        return client.isClosed();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public Client getClient() {
        return client;
    }
}
