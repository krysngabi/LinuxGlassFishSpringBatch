package com.abovebytes.server.dtos;

import lombok.Data;

@Data
public class ErrorServer {
    private int errorCode;
    private String errorMEssage;
}
