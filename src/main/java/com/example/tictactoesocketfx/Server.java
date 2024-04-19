package com.example.tictactoesocketfx;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;



        public void start(int port) {
//            Game.ticTacInstance.startGame();
        try {
            serverSocket = new ServerSocket(port); //server create for once
            System.out.println("Server started on port " + port);
            while (true){ //server accept is gone for a infinite loop...
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress()); //when server is available to contact with a client it will go further
//                or else it will sit down here and wait for the client
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            handleClient(); //in this method server will handle client [read/write]
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //stop();
        }
    }




        private void handleClient() throws IOException {
        try {
            Object inputObject;
            Scanner scanner= new Scanner(System.in);
            while ((inputObject = in.readObject()) != null) {
                // Handle client input
                System.out.println("client says: " + inputObject);
                // Echo back to the client
                System.out.println("Write back to client");
                String Smsg=scanner.nextLine();
                out.writeObject(Smsg);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }














//    public void start(int port) {
//        try {
//            serverSocket = new ServerSocket(port);
//            System.out.println("Server started on port " + port);
//            clientSocket = serverSocket.accept();
//            System.out.println("Client connected: " + clientSocket.getInetAddress());
//            out = new ObjectOutputStream(clientSocket.getOutputStream());
//            in = new ObjectInputStream(clientSocket.getInputStream());
//            handleClient();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            stop();
//        }
//    }
//
//    private void handleClient() throws IOException {
//        try {
//            Object inputObject;
//            while ((inputObject = in.readObject()) != null) {
//                // Handle client input
//                System.out.println("Received: " + inputObject);
//                // Echo back to the client
//                out.writeObject("Server received: " + inputObject);
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stop() {
//        try {
//            in.close();
//            out.close();
//            clientSocket.close();
//            serverSocket.close();
//            System.out.println("Server stopped");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        Server server = new Server();
//        server.start(9999); // Choose your desired port number
//    }














}

