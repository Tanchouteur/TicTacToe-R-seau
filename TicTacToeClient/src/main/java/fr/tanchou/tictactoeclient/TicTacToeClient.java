package fr.tanchou.tictactoeclient;

import java.io.IOException;

public class TicTacToeClient {
    GameModel gameModel;
    private final ServerConnection serverConnection;

    public TicTacToeClient(String serverAddress, int serverPort) throws IOException {
        serverConnection = new ServerConnection(serverAddress, serverPort);
    }

    public void startGame(TicTacToeUI ticTacToeUI) throws IOException {
        String serverMessage;
        String data = "";

        // Lire les messages du serveur et agir en conséquence
        while (!serverConnection.isClosed() && (serverMessage = serverConnection.readServerMessage()) != null) {

            String[] parsedMessage;

            try {
                parsedMessage = serverMessage.split(";");
            }catch (Exception e){
                System.err.println("Error parsing message " + serverMessage + " - err : " + e.getMessage());
                continue;
            }

            if (parsedMessage.length == 2) {
                data = parsedMessage[1];
            }

            // Agir en fonction du type de message (convertir en enum

            switch (MessageType.valueOf(parsedMessage[0])) {
                case PASSWORD:
                    serverConnection.sendToServer("1234");
                    break;

                case ENEMYMOVE:
                    gameModel.enemyMove(data);
                    break;

                case GAMESTATUS:
                    switch (data) {
                        case "WAITING" -> System.out.println("Waiting for player...");
                        case "STARTED" -> gameModel = new GameModel();
                        case "FINISHED" -> System.out.println("Game finished!");
                        case "yourturn" -> gameModel.setMyTurn(true);
                    }
                    break;

                case RESPONDSTATUS:
                    if (data == null) {
                        throw new RuntimeException("Error receiving message from server");
                    }

                    if (data.contains("false")) {

                        gameModel.cancelLastMove();
                        gameModel.setMyTurn(true);
                        return;

                    }else if (data.contains("true")) {

                        gameModel.setMyTurn(false);
                        gameModel.clearLastMove();
                        return;
                    }

                default:
                    break;
            }

            ticTacToeUI.updateUI();
        }

        serverConnection.close();
    }

    public void play(int row, int col) {
        if (!gameModel.isMyTurn()) {
            return;
        }
        gameModel.play(row, col);
        serverConnection.sendToServer("played;" + row + "," + col);
    }
}
