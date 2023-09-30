package com.codebaker.controllers;

import com.codebaker.model.Message;
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

    public ConnectedClient(Socket socket) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.socket = socket;

    }

    private void awaitMessage() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String inputString;
            while ((inputString = input.readLine()) != null) {
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
        connectedClients.forEach((client) -> {
            client.receiveMessage(json);
        });
    }

    public boolean receiveMessage(String json) {
        try {
            output.write(json);
            return true;
        } catch (IOException e) {
            close();
        }
        return false;
    }

    @Override
    public void run() {
        Timer timout = new Timer();
        timout.schedule(new TimerTask() {
            @Override
            public void run() {

                close();

            }
        }, 3000);


        timout.cancel();
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
