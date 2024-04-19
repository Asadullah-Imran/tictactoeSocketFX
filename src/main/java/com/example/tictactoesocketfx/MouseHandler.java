package com.example.tictactoesocketfx;

import javafx.scene.input.MouseEvent;

public class MouseHandler {
    private Game game;
    double mouseX, mouseY;

    public MouseHandler(Game game) {
        this.game =game;
        // Registering mouse event handlers
        game.gameScene.setOnMouseClicked(this::handleMouseClicked);
        // You can register other mouse event handlers similarly
    }

    private void handleMouseClicked(MouseEvent event) {
         mouseX = event.getX();
         mouseY = event.getY();
        System.out.println("x: " + mouseX + " y: " + mouseY);
    }

    // You can add other mouse event handlers as needed
}

