package com.sourabh.awesometic_tac_toe.model.ai;

import com.sourabh.awesometic_tac_toe.model.GameModel;
import com.sourabh.awesometic_tac_toe.model.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused", "MethodCanBeVariableArityMethod", "SameParameterValue"})
public class AIOverlord {
    private char[][] TTTBoard;

    private Player playerComputer;
    private Player humanOpponent; //Human

    private GameModel gameModel = new GameModel();

    public AIOverlord(GameModel gameModel) {
        this.TTTBoard = gameModel.getTTTBoard();
    }

    public void setPlayers(Player player) {
        this.humanOpponent = (player == Player.X) ? Player.O : Player.X;
        this.playerComputer = player;
    }

    /**
     * @return the best more {row, col}
     */
    public int move() {
        int[] result = minimax(2, playerComputer, TTTBoard); //Depth, Maximizing Player, TTT Board
        return 0;
    }

    private int[] minimax(int depth, Player player, char[][] tttBoard) {
        List<int[]> moves = findPossibleMoves(tttBoard);


        return new int[]{};
    }

    private List<int[]> findPossibleMoves(char[][] tttBoard) {
        List<int[]> nextMoves = new ArrayList<>();

        //There will be no moves if the game is over
        if (hasComputerWon() || hasHumanWon()) {
            return nextMoves; // Return empty list
        }

        for (int row = 0; row < tttBoard.length; row++) {
            for (int col = 0; col < tttBoard[row].length; col++) {
                if (TTTBoard[row][col] == ' ') {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }


    /**
     * Check if there is a winner
     * @return true if computer or if player has won
     */
    private boolean hasComputerWon() {
        return checkWin(playerComputer.toString().charAt(0));
    }

    private boolean hasHumanWon() {
        return checkWin(humanOpponent.toString().charAt(0));
    }

    private boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            //Row check
            if (TTTBoard[i][0] == player
                    && TTTBoard[i][1] == player
                    && TTTBoard[i][2] == player) {
                return true;
            }

            //Column Check
            if (TTTBoard[0][i] == player
                    && TTTBoard[1][i] == player
                    && TTTBoard[2][i] == player) {
                return true;
            }
        }

        if (TTTBoard[0][0] == player //Left Diagonal Check
                && TTTBoard[1][1] == player
                && TTTBoard[2][2] == player) {
            return true;
        }
        if (TTTBoard[0][2] == player //Right Diagonal Check
                && TTTBoard[1][1] == player
                && TTTBoard[2][0] == player) {
            return true;
        }
        return false;
    }

}
