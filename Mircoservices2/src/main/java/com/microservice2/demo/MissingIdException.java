package com.microservice2.demo;

public class MissingIdException extends RuntimeException {
    public MissingIdException(long id){
        super("User : " + id + "does not exist");
    }
}
