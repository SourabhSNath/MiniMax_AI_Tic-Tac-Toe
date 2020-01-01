package com.sourabh.awesometic_tac_toe.model.ai;

import com.sourabh.awesometic_tac_toe.model.GameModel;
import com.sourabh.awesometic_tac_toe.model.Player;

/**
 * An AI made with minimax algorithm and alpha-beta pruning
 */
@SuppressWarnings("WeakerAccess")
abstract class MiniMaxAI {

    protected int row = GameModel.row;
    protected int col = GameModel.col;

    protected Player aiMark;
    protected Player opponentMark;

    public MiniMaxAI(GameModel gameModel) {
//        TTTCells = gameModel.getTTTCells();
    }

    public void setMarkSymbol(Player mark) {
        this.aiMark = mark;
        opponentMark = (aiMark == Player.O) ? Player.X : Player.O;
    }

    abstract public int[] move();

}
