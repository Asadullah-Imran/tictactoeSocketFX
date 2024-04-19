package com.example.tictactoesocketfx;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class Player {
    private int posX;
    private int posY;
    private int speed = 5; // Adjust as needed

    public Player(int initialX, int initialY) {
        this.posX = initialX;
        this.posY = initialY;
    }

    public void move(KeyCode key) {
        switch (key) {
            case W:
                posY -= speed;
                break;
            case A:
                posX -= speed;
                break;
            case S:
                posY += speed;
                break;
            case D:
                posX += speed;
                break;
        }
    }

    public void moveTo(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}

