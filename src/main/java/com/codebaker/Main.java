package com.codebaker;


import com.codebaker.controllers.SocketClient;
import com.codebaker.controllers.SocketServer;
import com.codebaker.model.Person;
import com.codebaker.utils.Checks;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;


public class Main {

    public static void main(String[] args) {

        Scanner scanner =  new Scanner(System.in);
        System.out.print("please chose your user type (server or client) : ");
        String usertype = scanner.nextLine();

        if(usertype.equals("client")) {
            System.out.println("please provide your username :");

            try {
                SocketClient client = new SocketClient(scanner.nextLine());
                client.startClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else if(usertype.equals("server")) {
            try {
                SocketServer server = new SocketServer(9000);
                server.startServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("Error : invalid input");
        }



    }
}