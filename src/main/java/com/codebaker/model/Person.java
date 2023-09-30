package com.codebaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Person {
    private String name;
    private String address;
    private int age;
    private String sex;

    public Person(String name, String address, int age, String sex) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.sex = sex;
    }
}
