package com.sourabh.awesometic_tac_toe.model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Stack;

public class GameModel {

    private final String TAG = this.getClass().getName();

    private Cell[][] TTTCells = new Cell[3][3];
    private int row, col;
    private Player currentPlayer, winner;
    private int player1Score = 0, player2Score = 0;
    private GameStates gameState;

    private static final String player1 = "Player 1",
            player2 = "Player 2",
            tie = "Tied!";

    //To pass the instructions from switchPlayer() to the viewModel
    private static MutableLiveData<String> playerInstructionMutableLiveData = new MutableLiveData<>();

    // To pass the winner and scores to the viewModel
    private MutableLiveData<Integer> player1ScoreMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> player2ScoreMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> winnerMutableLiveData = new MutableLiveData<>();

    private Stack<String> undoStack;


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
                TTTCells[i][j] = new Cell();
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

        this.row = row;
        this.col = col;

        Log.d(TAG, "mark: " + currentPlayer.toString());

        TTTCells[row][col].setValue(currentPlayer);
        undoStack.push(String.format("%d%d", row, col));

        return currentPlayer;
    }

    //Switch the player if the current Player is Player 1
    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;

        Log.d(TAG, "switchPlayer: switched" + currentPlayer.toString());
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

    //Increment drawCounter here if the value is null
    private boolean isCellNotEmpty(int row, int col) {
        return TTTCells[row][col].getValue() != null; //return true if not null
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

    //Check if board is full
    private boolean isGameTie() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TTTCells[i][j].getValue() == null) {
                    return false; //False if any of the cell is empty
                }
            }
        }
        Log.d(TAG, "winner: Game Tied!");
        gameState = GameStates.IS_OVER; //Game wouldn't stop without this
        winner = null;
        return true;
    }

    private boolean isGameWon(Player currentPlayer, int row, int col) {

        return (TTTCells[row][0].getValue() == currentPlayer //Row Check
                && TTTCells[row][1].getValue() == currentPlayer
                && TTTCells[row][2].getValue() == currentPlayer

                || TTTCells[0][col].getValue() == currentPlayer //Column Check
                && TTTCells[1][col].getValue() == currentPlayer
                && TTTCells[2][col].getValue() == currentPlayer

                || row == col //DO only if this condition's met
                && TTTCells[0][0].getValue() == currentPlayer //Left Diagonal Check
                && TTTCells[1][1].getValue() == currentPlayer
                && TTTCells[2][2].getValue() == currentPlayer

                || row + col == 2
                && TTTCells[0][2].getValue() == currentPlayer //Right Diagonal Check
                && TTTCells[1][1].getValue() == currentPlayer
                && TTTCells[2][0].getValue() == currentPlayer);

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


    /**
     * Check if undo stack is empty to prevent EmptyStackException
     * Switch the player once again when undoing to prevent the current player from changing
     */
    private MutableLiveData<String> undoneMutableLiveData = new MutableLiveData<>();

    public LiveData<String> undoMove() {
        String undone = "";

        if (!undoStack.isEmpty()) {
            undone = undoStack.pop();
        }

        if (undone != null && undone.length() != 0) {
            int row = Integer.parseInt(String.valueOf(undone.charAt(0)));
            int col = Integer.parseInt(String.valueOf(undone.charAt(1)));

            Log.d(TAG, "undoMove: undoing " + row + " " + col);
            TTTCells[row][col].setValue(null);

            switchPlayer();

            undoneMutableLiveData.setValue(undone);
            return undoneMutableLiveData;
        }
        return undoneMutableLiveData;
    }
}