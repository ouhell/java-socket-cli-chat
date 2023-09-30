package com.codebaker.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    ServerSocket principalSocket;
    List<ConnectedClient> clients = new ArrayList<>();

    public SocketServer() throws IOException {
        this.principalSocket = new ServerSocket(9000);

    }


    public void startServer() throws IOException {

        while (!principalSocket.isClosed()) {
            Socket connectedSocket = principalSocket.accept();
            System.out.println("client connected :: " + connectedSocket.getInetAddress());
            clients.add(new ConnectedClient(connectedSocket));
        }
    }
}
