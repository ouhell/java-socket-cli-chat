package com.codebaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String content;
    private String sender;

    private String id;
}
