package com.sourabh.awesometic_tac_toe.model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Stack;

public class GameModel {

    private final String TAG = this.getClass().getName();

    private GameStates gameState;

    private Cell[][] TTTCell = new Cell[3][3];
    private Player currentPlayer;
    private Player winner;

    private int player1Score = 0;
    private int player2Score = 0;
    private static final String player1 = "Player 1";
    private static final String player2 = "Player 2";
    private static final String tie = "Tied!";

    //To pass the instructions from the switch player method
    private static MutableLiveData<String> playerInstructionMutableLiveData = new MutableLiveData<>();

    // To pass the winner and scores
    private MutableLiveData<Integer> player1ScoreMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> player2ScoreMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> winnerMutableLiveData = new MutableLiveData<>();


    private Stack<String> undoStack;

    private int row;
    private int col;

    public GameModel() {
        currentPlayer = Player.X;
        restart();

        gameState = GameStates.IS_RUNNING;
    }

    private void restart() {
        winner = null;
        winnerMutableLiveData.setValue(null);
        undoStack = new Stack<>();

        playerInstructionMutableLiveData.setValue("Player 1's turn");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TTTCell[i][j] = new Cell();
            }
        }

    }


    /**
     * Perform operation if the move is valid
     *
     * @param row 0-2
     * @param col 0-2
     * @return player
     */
    @SuppressLint("DefaultLocale")
    public Player mark(int row, int col) {

        Player justPlayed;
        this.row = row;
        this.col = col;


        Log.d(TAG, "mark: " + currentPlayer.toString());

        TTTCell[row][col].setValue(currentPlayer);
        justPlayed = currentPlayer;

        undoStack.push(String.format("%d%d", row, col));


        return justPlayed;
    }

    //Switch the player if the current Player is Player 1
    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;

        if (currentPlayer == Player.X) {
            playerInstructionMutableLiveData.setValue("Player 1's turn");
        } else {
            playerInstructionMutableLiveData.setValue("Player 2's turn");
        }
    }

    public boolean isValid(int row, int col) {

        if (gameState == GameStates.IS_OVER) {
            return false;
        } else if (isCellNotEmpty(row, col)) {
            return false;
        } else if (row > 2 || col > 2 || row < 0 || col < 0) {
            return false;
        } else
            return true;
    }

    private boolean isCellNotEmpty(int row, int col) {
        return TTTCell[row][col].getValue() != null; //return true if not null
    }


    //Check if the game has end with a winning move or a tie
    public boolean isGameEnd() {
        if (isGameWon(currentPlayer, row, col)) {
            winner = currentPlayer;
            Log.d(TAG, "winner: " + winner.toString());
            gameState = GameStates.IS_OVER;
            return true;
        }
        return isGameTie();
    }


    private boolean isGameWon(Player currentPlayer, int row, int col) {

        return (TTTCell[row][0].getValue() == currentPlayer //Row Check
                && TTTCell[row][1].getValue() == currentPlayer
                && TTTCell[row][2].getValue() == currentPlayer

                || TTTCell[0][col].getValue() == currentPlayer //Column Check
                && TTTCell[1][col].getValue() == currentPlayer
                && TTTCell[2][col].getValue() == currentPlayer

                || row == col
                && TTTCell[0][0].getValue() == currentPlayer //Left Diagonal Check
                && TTTCell[1][1].getValue() == currentPlayer
                && TTTCell[2][2].getValue() == currentPlayer

                || row + col == 2
                && TTTCell[0][2].getValue() == currentPlayer //Right Diagonal Check
                && TTTCell[1][1].getValue() == currentPlayer
                && TTTCell[2][0].getValue() == currentPlayer);

    }

    private boolean isGameTie() {
        int count = 1;
        for (Cell[] row : TTTCell) {
            for (Cell cell : row) {
                if (cell == null) {
                    Log.d(TAG, "isGameTie: entered in here");
                    ++count;
                }
            }
        }
        winner = null;
        if (count == 9) {
            gameState = GameStates.IS_OVER;
            return true;
        }
        return false;
    }


    public LiveData<Integer> player1Score() {
        player1ScoreMutableLiveData.setValue(player1Score);
        if (winner == Player.X) {
            player1Score++;
            player1ScoreMutableLiveData.setValue(player1Score);
        }
        return player1ScoreMutableLiveData;
    }


    public LiveData<Integer> player2Score() {
        player2ScoreMutableLiveData.setValue(player2Score);
        if (winner == Player.O) {
            player2Score++;
            player2ScoreMutableLiveData.setValue(player2Score);
        }
        return player2ScoreMutableLiveData;
    }

    public LiveData<String> winner() {
        if (winner != null) {
            winnerMutableLiveData.setValue((winner == Player.X) ? player1 : player2);
            return winnerMutableLiveData;
        } else {
            winnerMutableLiveData.setValue(tie);
            return winnerMutableLiveData;
        }
    }

    public LiveData<String> playerInstruction() {
        return playerInstructionMutableLiveData;
    }

    public String undoMove() {
        String undone = "";

        if (!undoStack.isEmpty()) {
            undone = undoStack.pop();
        }

        if (undone != null && undone.length() != 0) {
            int row = Integer.parseInt(String.valueOf(undone.charAt(0)));
            int col = Integer.parseInt(String.valueOf(undone.charAt(1)));

            Log.d(TAG, "undoMove: undoing " + row + " " + col);
            TTTCell[row][col].setValue(null);
            return undone;
        }
        return undone;
    }
}
