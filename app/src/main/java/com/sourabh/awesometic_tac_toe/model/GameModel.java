package com.sourabh.awesometic_tac_toe.model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Stack;

public class GameModel {

    private final String TAG = this.getClass().getName();

    private char[][] TTTCell = new char[3][3];
    static public int row, col;

    private Player currentPlayer, winner;
    private int player1Score = 0, player2Score = 0;
    private GameStates gameState;

    private static final String player1 = "Player 1",
            player2 = "Player 2",
            tie = "Tied!";

    private MutableLiveData<Player> currentPlayerMutableLiveData = new MutableLiveData<>();

    //To pass the instructions from switchPlayer() to the viewModel
    private MutableLiveData<String> playerInstructionMutableLiveData = new MutableLiveData<>();

    // To pass the winner and scores to the viewModel
    private MutableLiveData<Integer> player1ScoreMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> player2ScoreMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> winnerMutableLiveData = new MutableLiveData<>();

    private Stack<String> undoStack;


    public GameModel() {
        currentPlayer = Player.X;
        winner = null;
        winnerMutableLiveData.setValue(null);
        undoStack = new Stack<>();

        playerInstructionMutableLiveData.setValue("Player 1's turn");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TTTCell[i][j] = ' ';
            }
        }
        gameState = GameStates.IS_RUNNING;
    }

    /**
     * Marks the Player on the board
     * Pushes row and column info on to the stack
     *
     * @param row 0-2, passed from GameViewModel
     * @param col 0-2, also passed from GameViewModel
     * @return currentPlayer, which will be marked on the UI through dataBinding
     */
    @SuppressLint("DefaultLocale")
    public LiveData<Player> mark(int row, int col) {

        GameModel.row = row;
        GameModel.col = col;

        Log.d(TAG, "mark: " + currentPlayer.toString());

        TTTCell[row][col] = (currentPlayer == Player.X) ? 'x' : 'o';
        undoStack.push(String.format("%d%d", row, col));

        currentPlayerMutableLiveData.setValue(currentPlayer);
        return currentPlayerMutableLiveData;
    }

    /**
     * Used in GameViewModel class to switch player if game isn't over
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;

        Log.d(TAG, "switchPlayer: switched" + currentPlayer.toString());
        if (currentPlayer == Player.X) {
            playerInstructionMutableLiveData.setValue("Player 1's turn");
        } else {
            playerInstructionMutableLiveData.setValue("Player 2's turn");
        }
    }

    /**
     * Used in the GameViewModel to check if the current move is valid
     */
    public boolean isValid(int row, int col) {

        if (gameState == GameStates.IS_OVER) {
            return false;
        } else if (isCellNotEmpty(row, col)) {
            return false;
        } else
            return true;
    }

    private boolean isCellNotEmpty(int row, int col) {
        return TTTCell[row][col] != ' '; //return true if not null
    }

    /**
     * Check if the game has end with a winning move or a tie
     *
     * @return true if the Game is over
     */
    public boolean isGameEnd() {

        if (isGameWon(currentPlayer, row, col)) {
            winner = currentPlayer;
            Log.d(TAG, "winner: " + winner.toString());
            gameState = GameStates.IS_OVER;
            return true;
        }
        return isGameTie();
    }

    /**
     * Check if board is full
     *
     * @return false if a cell is empty
     * else set the GameState to IS_OVER
     */
    private boolean isGameTie() {

        for (char[] row : TTTCell) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }
        Log.d(TAG, "winner: Game Tied!");
        gameState = GameStates.IS_OVER; //Game wouldn't stop without this
        winner = null;
        return true;
    }

    /**
     * Check if the Current Player has won the game
     *
     * @param row current row position
     * @param col current column position
     * @return true if current player wins the game
     */
    private boolean isGameWon(Player player, int row, int col) {

        char currentPlayer = (player == Player.X) ? 'x' : 'o';

        return TTTCell[row][0] == currentPlayer//Row Check
                && TTTCell[row][1] == currentPlayer
                && TTTCell[row][2] == currentPlayer

                || TTTCell[0][col] == currentPlayer //Column Check
                && TTTCell[1][col] == currentPlayer
                && TTTCell[2][col] == currentPlayer

                || row == col //False if this condition isn't met
                && TTTCell[0][0] == currentPlayer //Left Diagonal Check
                && TTTCell[1][1] == currentPlayer
                && TTTCell[2][2] == currentPlayer

                || row + col == 2 //False if this condition isn't met
                && TTTCell[0][2] == currentPlayer //Right Diagonal Check
                && TTTCell[1][1] == currentPlayer
                && TTTCell[2][0] == currentPlayer;

    }

    /**
     * Passing all the below return values through LiveData to the GameViewModel
     */
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
     * <p>
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

            TTTCell[row][col] = ' ';
            switchPlayer();

            undoneMutableLiveData.setValue(undone);
            return undoneMutableLiveData;
        }
        return undoneMutableLiveData;
    }


}