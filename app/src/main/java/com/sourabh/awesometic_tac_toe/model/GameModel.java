package com.sourabh.awesometic_tac_toe.model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Stack;

public class GameModel {

    private final String TAG = this.getClass().getName();

    private char[][] TTTBoard = new char[3][3];
    private static int row, col;

    private Player currentPlayer, winner;
    private int player1Score = 0, player2Score = 0;

    private enum GameStates {IS_OVER, IS_RUNNING}

    private GameStates gameState;

    private static final String PLAYER_1 = "Player 1",
            PLAYER_2 = "Player 2",
            TIED = "Tied!";

    //To pass the instructions from switchPlayer() to the viewModel
    private MutableLiveData<String> playerInstructionMutableLiveData = new MutableLiveData<>();


    private Stack<int[]> undoStack;

    //Constructor
    public GameModel() {
        winner = null;
        undoStack = new Stack<>();

        restart();

    }

    public void restart() {
        currentPlayer = Player.X;
        playerInstructionMutableLiveData.setValue("Player 1's turn");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TTTBoard[i][j] = ' ';
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
        MutableLiveData<Player> currentPlayerMutableLiveData = new MutableLiveData<>();

        GameModel.row = row;
        GameModel.col = col;

        Log.d(TAG, "mark: " + currentPlayer.toString());

        TTTBoard[row][col] = currentPlayer.toString().charAt(0);

        undoStack.push(new int[]{row, col});

        currentPlayerMutableLiveData.setValue(currentPlayer);
        return currentPlayerMutableLiveData;
    }

    /**
     * Used in GameViewModel class to switch player if game isn't over
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;

        Log.d(TAG, "switchPlayer: switched " + currentPlayer.toString());
        if (currentPlayer == Player.X) {
            Log.d(TAG, "switchPlayer: to x");
            playerInstructionMutableLiveData.setValue("Player 1's turn");
        } else {
            Log.d(TAG, "switchPlayer: to o");
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
        return TTTBoard[row][col] != ' '; //return true if not null
    }

    /**
     * Check if the game has end with a winning move or a TIED
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

//    /**
//     * Overloading
//     * @param currentPlayer from the AI class
//     * row & col from the AI class
//     * @return true if Game is Over
//     */
//    public boolean isGameEnd(Player currentPlayer, int row, int col) {
//        if (isGameWon(currentPlayer, row, col)) {
//            winner = currentPlayer;
//            Log.d(TAG, "winner: " + winner.toString());
//            gameState = GameStates.IS_OVER;
//            return true;
//        }
//        return isGameTie();
//    }

    /**
     * Check if board is full
     *
     * @return false if a cell is empty
     * else set the GameState to IS_OVER
     */
    private boolean isGameTie() {

        for (char[] row : TTTBoard) {
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

        char currentPlayer = player.toString().charAt(0);

        return TTTBoard[row][0] == currentPlayer//Row Check
                && TTTBoard[row][1] == currentPlayer
                && TTTBoard[row][2] == currentPlayer

                || TTTBoard[0][col] == currentPlayer //Column Check
                && TTTBoard[1][col] == currentPlayer
                && TTTBoard[2][col] == currentPlayer

                || row == col //False if this condition isn't met
                && TTTBoard[0][0] == currentPlayer //Left Diagonal Check
                && TTTBoard[1][1] == currentPlayer
                && TTTBoard[2][2] == currentPlayer

                || row + col == 2 //False if this condition isn't met
                && TTTBoard[0][2] == currentPlayer //Right Diagonal Check
                && TTTBoard[1][1] == currentPlayer
                && TTTBoard[2][0] == currentPlayer;

    }

    /**
     * Passing all the below return values through LiveData to the GameViewModel
     */
    public LiveData<Integer> player1Score() {
        MutableLiveData<Integer> player1ScoreMutableLiveData = new MutableLiveData<>();

        player1ScoreMutableLiveData.setValue(player1Score);
        if (winner == Player.X) {
            player1Score++;
            player1ScoreMutableLiveData.setValue(player1Score);
        }
        return player1ScoreMutableLiveData;
    }

    public LiveData<Integer> player2Score() {
        MutableLiveData<Integer> player2ScoreMutableLiveData = new MutableLiveData<>();

        player2ScoreMutableLiveData.setValue(player2Score);
        if (winner == Player.O) {
            player2Score++;
            player2ScoreMutableLiveData.setValue(player2Score);
        }
        return player2ScoreMutableLiveData;
    }

    public LiveData<String> winner() {
        MutableLiveData<String> winnerMutableLiveData = new MutableLiveData<>();

        if (winner != null) {
            winnerMutableLiveData.setValue((winner == Player.X) ? PLAYER_1 : PLAYER_2);
            return winnerMutableLiveData;
        } else {
            winnerMutableLiveData.setValue(TIED);
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
     * since switchPlayer() is called in the viewModel onCellClicked() too
     */
    public LiveData<int[]> undoMove() {
        MutableLiveData<int[]> undoneMutableLiveData = new MutableLiveData<>();

        int[] undone = new int[2];

        if (!undoStack.isEmpty())
            undone = undoStack.pop();

        if (undone != null) {
            int row = undone[0];
            int col = undone[1];

            Log.d(TAG, "undoMove: undoing " + row + " " + col);

            if (isCellNotEmpty(row, col)) {
                switchPlayer();
            }

            TTTBoard[row][col] = ' ';

            undoneMutableLiveData.setValue(undone);
            return undoneMutableLiveData;
        }
        return undoneMutableLiveData;
    }

    public char[][] getTTTBoard() {
        return TTTBoard;
    }
}