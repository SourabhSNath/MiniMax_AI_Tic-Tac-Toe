package com.sourabh.awesometic_tac_toe.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sourabh.awesometic_tac_toe.model.GameModel;
import com.sourabh.awesometic_tac_toe.model.Player;

import java.util.Objects;

public class GameViewModel extends ViewModel {

    private GameModel gameModel;
    public ObservableArrayMap<String, String> markCellText = new ObservableArrayMap<>();
    private MutableLiveData<Integer> player1Score = new MutableLiveData<>();
    private MutableLiveData<Integer> player2Score = new MutableLiveData<>();

    private boolean undoCheck;

    public GameViewModel() {
        this.gameModel = new GameModel();
        getPlayer1Score();
        getPlayer2Score();
        undoCheck = false;
    }

    @SuppressLint("DefaultLocale")
    public void onCellClicked(int row, int column) {

        Log.d("OnClick", "onCellClicked: " + row + " " + column);

        if (gameModel.isValid(row, column)) {

            if (undoCheck) {

                markCellText.put(String.format("%d%d", row, column), "");
                undoCheck = false;

            } else {

                Player player = gameModel.mark(row, column);
                if (player != null) {

                    markCellText.put(String.format("%d%d", row, column), player.toString());

                    if (gameModel.isGameEnd()) {
                        winner();

                    } else{
                        Log.d("onClick", "onCellClicked: switched");
                        gameModel.switchPlayer();
                    }
                }

            }
        }

    }


    public void onUndo() {
        String undoRowCol = gameModel.undoMove();
        if (undoRowCol != null && undoRowCol.length() != 0) {
            int row = Integer.parseInt(String.valueOf(undoRowCol.charAt(0)));
            int col = Integer.parseInt(String.valueOf(undoRowCol.charAt(1)));
            undoCheck = true;
            onCellClicked(row, col);
        }
    }


    public LiveData<String> winner() {
        if (gameModel.winner() != null || Objects.equals(gameModel.winner().getValue(), "Tied!")) {
            return gameModel.winner();
        }
        return new MutableLiveData<>("");
    }

    public LiveData<Integer> getPlayer1Score() {
        player1Score.setValue(gameModel.player1Score().getValue());
        return player1Score;
    }

    public LiveData<Integer> getPlayer2Score() {
        player2Score.setValue(gameModel.player2Score().getValue());
        return player2Score;
    }

    public LiveData<String> getPlayerInstruction() {
        return gameModel.playerInstruction();
    }


}
