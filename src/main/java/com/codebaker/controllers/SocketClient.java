package com.codebaker.controllers;

import com.codebaker.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    private String username;
    private Socket socket;

    private BufferedReader input;
    private BufferedWriter output;

    private ObjectMapper mapper;

    public SocketClient(String username) throws IOException {
        this.socket = new Socket("192.168.1.3", 9000);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.mapper = new ObjectMapper();
        this.username = username;
    }

    private void awaitMessages() {
        try {
            String inputString;
            while ((inputString = input.readLine()) != null) {
                Message message = mapper.readValue(inputString, Message.class);
                System.out.printf("%s : %s", message.getSender(), message.getContent());
                System.out.println();
            }
        } catch (JsonProcessingException e) {

        } catch (IOException e) {
            close();
        }
    }

    private void listenForClientInput() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            if (!message.isEmpty() && !message.isBlank()) {
                sendMessage(message);
            }
        }
        scanner.close();
    }

    private void sendMessage(String msg) {
        Message message = new Message(msg, username, "random");
        try {
            String json = mapper.writeValueAsString(message);
            this.output.write(json);
            this.output.newLine();
            this.output.flush();
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {

            close();
        }

    }

    public void startClient() {
        try {
            this.output.write(username);
            this.output.newLine();
            this.output.flush();

        } catch (IOException e) {
            close();
        }
        Thread inputListener = new Thread(this::listenForClientInput);
        inputListener.start();
        awaitMessages();
    }


    public void close() {

        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}
