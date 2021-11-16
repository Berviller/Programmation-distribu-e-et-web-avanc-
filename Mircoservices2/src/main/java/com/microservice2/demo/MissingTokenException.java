package com.microservice2.demo;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException(String tk){
        super("Token does not exist");
    }
}
