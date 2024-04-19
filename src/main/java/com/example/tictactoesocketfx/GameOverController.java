package com.example.tictactoesocketfx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class GameOverController {
    public Button playAgainBtn;
    public Button backBtn;
    Game game=  Game.ticTacInstance;

    public void onPlayAgain(ActionEvent actionEvent) {

    }

    public void onBack(ActionEvent actionEvent) throws IOException {
        game.showMenuPage();
    }
}
