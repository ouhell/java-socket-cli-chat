package com.codebaker;


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
        ObjectMapper mapper = new ObjectMapper();
        try {
            Person p = new Person("oussama", "homelss mf", 23, "male");
            String res = mapper.writeValueAsString(p);
            System.out.println(res);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}