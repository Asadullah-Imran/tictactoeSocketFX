package com.example.tictactoesocketfx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {
    public Button btnServer;
    public Button btnClient;
    public Button btnBack;

    public void onServerBtn(ActionEvent actionEvent) throws IOException {
        new Game().showServerPage();
    }

    public void onClientBtn(ActionEvent actionEvent) throws IOException {
        new Game().showClientPage();
    }

    public void onBackBtn(ActionEvent actionEvent) {
        new Game().exit();
    }
}
