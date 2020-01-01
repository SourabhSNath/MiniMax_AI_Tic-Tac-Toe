package com.sourabh.awesometic_tac_toe.model.ai;

import com.sourabh.awesometic_tac_toe.model.GameModel;

public class AIOverlord extends MiniMaxAI {

    // Moves {row, col} in order of preferences. {0, 0} at top-left corner
    private int[][] preferredMoves = {
            {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
            {0, 1}, {1, 0}, {1, 2}, {2, 1}};

    public AIOverlord(GameModel gameModel) {
        super(gameModel);
    }

    @Override
    int[] move() {
        for (int[] move : preferredMoves) {
            if (TTTCells[0][move[1]].isEmpty()) {
                return move;
            }
        }
        return null;
    }
}
