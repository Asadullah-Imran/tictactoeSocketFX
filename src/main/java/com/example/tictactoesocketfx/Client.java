package com.example.tictactoesocketfx;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private Socket socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void startConnection(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public void start(){
//    Game.ticTacInstance.startGame();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Write to server");
            String msg=scanner.nextLine();
            sendMessage(msg);
            System.out.println("Server says: "+receiveMessage().toString());
        }
}
    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receiveMessage() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        Client client = new Client();
//        client.startConnection("localhost", 9999); // Replace "localhost" with the server IP address
//        client.sendMessage("Hello, server!");
//        Object response = client.receiveMessage();
//        System.out.println("Server response: " + response);
//        client.stopConnection();
//    }
}
