package fr.tanchou.tictactoeclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TicTacToeUI extends JFrame {
    private final TicTacToeClient client;
    private final JButton[][] buttons;

    public TicTacToeUI(String serverAddress, int serverPort) throws IOException {
        client = new TicTacToeClient(serverAddress, serverPort);
        buttons = new JButton[3][3];

        // Configuration de la fenêtre
        setTitle("Tic Tac Toe");
        setLayout(new GridLayout(3, 3));
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation des boutons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setEnabled(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }

        setVisible(true);

        try {
            this.client.startGame(this);
        } catch (IOException e) {
            System.err.println("Error starting game: " + e.getMessage());
        }
    }

    public void updateUI() {
        if (client.gameModel == null) {
            return;
        }
        Board board = client.gameModel.getBoard();
        int[][] boardArray = board.getBoard();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (client.gameModel.isMyTurn()) {
                    switch (boardArray[i][j]) {
                        case 1:
                            buttons[i][j].setText("X");
                            buttons[i][j].setEnabled(false);
                            break;
                        case 2:
                            buttons[i][j].setText("O");
                            buttons[i][j].setEnabled(false);
                            break;
                        default:
                            buttons[i][j].setText(" ");
                            buttons[i][j].setEnabled(true);
                            break;
                    }
                } else {
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            client.play(row, col);
            updateUI();
        }
    }

    public static void main(String[] args) throws IOException {
        new TicTacToeUI("129.151.251.156", 8092);
    }
}
