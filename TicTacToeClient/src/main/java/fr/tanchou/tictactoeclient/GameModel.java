package fr.tanchou.tictactoeclient;

public class GameModel {

    private final Board board;

    boolean gameOver = false;

    private String lastMove = "";

    private boolean isMyTurn = false;

    public GameModel() {
        this.board = new Board();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void play(int row, int col) {
        this.board.placeMove(row, col, 1);
        this.lastMove = row + "," + col;
    }

    public void enemyMove(String data) {
        String[] move = data.split(",");
        this.board.placeMove(Integer.parseInt(move[0]), Integer.parseInt(move[1]), 2);
    }

    public Board getBoard() {
        return this.board;
    }

    public void cancelLastMove() {
        String[] move = this.lastMove.split(",");
        this.board.placeMove(Integer.parseInt(move[0]), Integer.parseInt(move[1]), 0);
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public void clearLastMove() {
        this.lastMove = "";
    }
}
