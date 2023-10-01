package com.codebaker.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    ServerSocket principalSocket;
    int port ;

    public SocketServer(int port) throws IOException {
        this.principalSocket = new ServerSocket(port);
        this.port = port;
    }


    public void startServer() throws IOException {
        System.out.println("Server Started On Port :: "+ this.port);
        while (!principalSocket.isClosed()) {
            Socket connectedSocket = principalSocket.accept();
            System.out.println("client connected :: " + connectedSocket.getInetAddress());

        }
    }
}
