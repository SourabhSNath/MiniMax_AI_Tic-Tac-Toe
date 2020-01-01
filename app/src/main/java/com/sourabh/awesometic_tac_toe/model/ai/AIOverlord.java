package com.sourabh.awesometic_tac_toe.model.ai;

import com.sourabh.awesometic_tac_toe.model.GameModel;

public class AIOverlord extends MiniMaxAI {

    public AIOverlord(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * @return the best more {row, col}
     */
    @Override
    public int[] move() {
        return new int[0];
    }


}
