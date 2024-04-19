package com.example.tictactoesocketfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Game extends Application {
    private int playerMove = -1;

    public int playerState;
    public final int serverState=1;
    public final int clientState=2;
    static Stage primaryStage;
    static Game ticTacInstance;
    private Player player;
    private final int WIDTH = 506;
    private final int HEIGHT = 527;
    public String ip;
    public int port;
    ServerSocket serverSocket;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Scene gameScene;
    Canvas canvas= new Canvas(WIDTH,HEIGHT);
    GraphicsContext gc= canvas.getGraphicsContext2D();

    //Game Properties
    public boolean isServer=false;
    // Add a boolean variable to track whose turn it is
    private boolean isServerTurn = true;
    public static boolean isConnected=false;

    //first start the game [show menu screen]



    //now try to implement other game methods
    private Image board;
    private Image redX;
    private Image blueX;
    private Image redCircle;
    private Image blueCircle;


    private String[] spaces = new String[9];

    private boolean yourTurn = false;
    private boolean circle = true;
    private boolean accepted = false;
    private boolean unableToCommunicateWithOpponent = false;
    private boolean won = false;
    private boolean enemyWon = false;
    private boolean tie = false;

    private int lengthOfSpace = 160;
    private int errors = 0;
    private int firstSpot = -1;
    private int secondSpot = -1;

    private String waitingString = "Waiting for another player";
    private String unableToCommunicateWithOpponentString = "Unable to communicate with opponent.";
    private String wonString = "You won!";
    private String enemyWonString = "Opponent won!";
    private String tieString = "Game ended in a tie.";

    private int[][] wins = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 }, { 2, 4, 6 } };


    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(" start method called");
//        serverPlayer = new Player(width / 2, height / 2);
//        clientPlayer = new Player(width / 2, height / 2);

        primaryStage=stage;
        ticTacInstance=this;


        showMenuPage();
    }


    public void startGame(int playerState,String ipAddress, int port){
        Pane pane = new Pane();
        gameScene= new Scene(pane,WIDTH,HEIGHT);
        pane.getChildren().add(canvas);
        primaryStage.setScene(gameScene);

        if(playerState==serverState){
            initializeServer(port);
        }else if(playerState==clientState){
            clientConnection(ipAddress, port);
        }
        draw();
        loadImage();

        new Thread(() -> {
            while (true) {
                draw();
                tick();
                try {
                    // Sleep for a short duration to reduce CPU usage
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
            canvas.setOnMouseClicked(event -> {
                if (accepted) {
                    if (yourTurn && !unableToCommunicateWithOpponent && !won && !enemyWon) {
                        int x = (int) (event.getX() / lengthOfSpace);
                        int y = (int) (event.getY() / lengthOfSpace);
                        y *= 3;
                        int position = x + y;

                        if (spaces[position] == null) {
                            if (!circle) spaces[position] = "X";
                            else spaces[position] = "O";
                            yourTurn = false;
                            draw();
                            try {
                                oos.writeInt(position);
                                oos.flush();
                            } catch (IOException e1) {
                                errors++;
                                e1.printStackTrace();
                            }

                            System.out.println("DATA WAS SENT");
                            checkForWin();
                            checkForTie();

                        }
                    }
                }
            });





    }

    private void tick() {
        if (errors >= 10) unableToCommunicateWithOpponent = true;

        if (!yourTurn && !unableToCommunicateWithOpponent) {
            try {
                int space = ois.readInt();
                if (circle) spaces[space] = "X";
                else spaces[space] = "O";
                checkForEnemyWin();
                checkForWin();
                checkForTie();
                yourTurn = true;
            } catch (IOException e) {
                e.printStackTrace();
                errors++;
            }
        }
    }
    // Draw the player moves with different shapes or colors
    public void draw() {
        gc.drawImage(board, 0, 0);
        		if (accepted) {
        			for (int i = 0; i < spaces.length; i++) {
        				if (spaces[i] != null) {
        					if (spaces[i].equals("X")) {
        						if (circle) {
//                                    gc.setFill(Color.RED);
                                    gc.drawImage(redX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3));
        						} else {
                                    //gc.setFill(Color.BLUE);
                                    //drawCross((i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3),lengthOfSpace/4);
                                    gc.drawImage(blueX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3));
        						}
        					} else if (spaces[i].equals("O")) {
        						if (circle) {
                                    //gc.setFill(Color.BLUE);
                                    //drawCircle((i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3),lengthOfSpace/3);
                                    gc.drawImage(blueCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3));
        						} else {
                                    //gc.setFill(Color.RED);
                                    //drawCircle((i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3),lengthOfSpace/3);
                                    gc.drawImage(redCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3));
        						}
        					}
        				}
        			}
        			if (won || enemyWon) {
                        gc.setFill(Color.BLACK);
        				gc.strokeLine(firstSpot % 3 * lengthOfSpace + 10 * firstSpot % 3 + lengthOfSpace / 2, (int) (firstSpot / 3) * lengthOfSpace + 10 * (int) (firstSpot / 3) + lengthOfSpace / 2, secondSpot % 3 * lengthOfSpace + 10 * secondSpot % 3 + lengthOfSpace / 2, (int) (secondSpot / 3) * lengthOfSpace + 10 * (int) (secondSpot / 3) + lengthOfSpace / 2);
                        gc.setFill(Color.RED);
        				if (won) {
                            gc.fillText(wonString,120,120);
        				} else if (enemyWon) {
                            gc.fillText(enemyWonString,120,120);
        				}
        			}
        			if (tie) {
                        gc.setFill(Color.BLACK);
                        gc.fillText(tieString,130,130);
        			}
        		} else {
                    gc.setFill(Color.RED);
                    gc.fillText(waitingString,100,100);
        		}
    }

    // Helper method to draw a circle
    private void drawCircle(int centerX, int centerY, int radius) {
        gc.strokeOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    // Helper method to draw a cross
    private void drawCross(int centerX, int centerY, int size) {
        gc.strokeLine(centerX - size, centerY - size, centerX + size, centerY + size);
        gc.strokeLine(centerX - size, centerY + size, centerX + size, centerY - size);
    }
    public void clientConnection(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
                accepted=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            yourTurn = true;
            circle = false;
            // Listen for a single client connection
            socket = serverSocket.accept();
            accepted=true;
            System.out.println("Client connected: " + socket.getInetAddress());

            // Initialize input and output streams for communication with the client
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void checkForWin() {
        for (int i = 0; i < wins.length; i++) {
            if (circle) {
                if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    won = true;
                }
            } else {
                if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    won = true;
                }
            }
        }
    }

    private void checkForEnemyWin() {
        for (int i = 0; i < wins.length; i++) {
            if (circle) {
                if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    enemyWon = true;
                }
            } else {
                if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    enemyWon = true;
                }
            }
        }
    }

    private void checkForTie() {
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] == null) {
                return;
            }
        }
        tie = true;
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
                        //<--------SHOW PAGE----->//
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
public void showMenuPage() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tictactoesocketfx/menu.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    System.out.println("show menu page called");
}
    public void showClientPage() throws IOException {
        System.out.println("show client page called");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tictactoesocketfx/client.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    public void showServerPage() throws IOException {
        System.out.println("show server page called");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tictactoesocketfx/server.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    //	private void loadImages() {
    //		try {
    //			board = ImageIO.read(getClass().getResourceAsStream("/board.png"));
    //			redX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
    //			redCircle = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
    //			blueX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
    //			blueCircle = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		}
    //	}
    private void loadImage(){
        board = new Image("/board.png");
        redX= new Image("/redX.png");
        redCircle = new Image("/redCircle.png");
        blueX= new Image("/blueX.png");
        blueCircle=new Image("/blueCircle.png");
    }


    public static void main(String[] args) {
        launch(args);
    }
    public void exit(){
        Platform.exit();
    }
}
