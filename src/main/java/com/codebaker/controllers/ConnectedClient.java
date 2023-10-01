package com.codebaker.controllers;

import com.codebaker.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectedClient implements Runnable {

    static private final List<ConnectedClient> connectedClients = new ArrayList<>();
    private final BufferedReader input;
    private final BufferedWriter output;

    private final Socket socket;

    private String username;

    public ConnectedClient(Socket socket) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.socket = socket;
        this.username = "";
    }

    private void awaitMessage() {
        ObjectMapper mapper = new ObjectMapper();
        try {

            while (this.socket.isConnected()) {
                String inputString = input.readLine();
                try {
                    Message message = mapper.convertValue(inputString, Message.class);
                    broadcastMessage(message, inputString);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (IOException e) {
            close();
        }

    }

    private void broadcastMessage(Message message, String json) {
        System.out.println("broad casting message from :" + this.username + "\n message : " + message.getContent());

        connectedClients.forEach((client) -> {
            if (client != this)
                client.receiveMessage(json);
        });
    }


    private void broadcastMessage(Message message) {
        System.out.println("broad casting message from :" + this.username + "\n message : " + message.getContent());
        try {
            String json = new ObjectMapper().writeValueAsString(message);
            connectedClients.forEach((client) -> {


                if (client != this)
                    client.receiveMessage(json);
            });
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());

        }

    }

    public boolean receiveMessage(String json) {
        try {
            output.write(json);
            output.newLine();
            return true;
        } catch (IOException e) {
            close();
        }
        return false;
    }

    @Override
    public void run() {
        System.out.println("run the client");
//        Timer timout = new Timer();
//        timout.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                close();
//
//            }
//        }, 3000);
//
//
//        timout.cancel();
        try {
            System.out.println("waiting for line");
            this.username = input.readLine();
            System.out.println(this.username + " connected to the chat;");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            close();
        }

        connectedClients.add(this);
        broadcastMessage(new Message(this.username + " has joined the chat.", "SERVER", "random"));
        awaitMessage();
    }

    public void close() {
        connectedClients.remove(this);
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}
