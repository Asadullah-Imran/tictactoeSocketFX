package com.example.tictactoesocketfx;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ServerController {
    public Button btnStart;
    public Button btnBack;
    public TextField portField;

    public void onStartBtn(ActionEvent actionEvent) {
        String portText = portField.getText();
        Game game = Game.ticTacInstance;

        // Check if the portText is a valid integer
        try {
            int serverPort = Integer.parseInt(portText);

            // Check if the port number is within the valid range
            if (serverPort >= 0 && serverPort <= 65535) {
                // Port number is valid, you can proceed with server start logic here

//                Server server= new Server();
//                server.start(serverPort);
                game.startGame(game.serverState,"",serverPort); //no need for ipaddress here;

            } else {
                // Port number is out of range, display an error message
                showAlert("Invalid Port Number", "Port number should be between 0 and 65535.");
            }
        } catch (NumberFormatException e) {
            // Input is not a valid integer, display an error message
            showAlert("Invalid Input", "Please enter a valid integer for the port number.");
        }
    }

    public void onBackBtn(ActionEvent actionEvent) throws IOException {
        new Game().showMenuPage();
    }
    // Helper method to display an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
