package com.example.tictactoesocketfx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ClientController {
    public TextField ipField;
    public TextField portField;
    public Button btnStart;
    public Button btnBack;
    public Label alertLabel;


    public void onStartBtn(ActionEvent actionEvent) {
        Game game=  Game.ticTacInstance;
        String ip= ipField.getText();
        int port = Integer.parseInt(portField.getText());
        //now create logic for start a gme for client view
        game.startGame(game.clientState,ip,port);
    }

    public void onBackBtn(ActionEvent actionEvent) throws IOException {
        new Game().showMenuPage();
    }
}
