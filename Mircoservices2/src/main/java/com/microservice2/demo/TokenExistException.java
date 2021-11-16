package com.microservice2.demo;

public class TokenExistException extends RuntimeException {
    public TokenExistException(long id){
        super("User already exist : " + id);
    }
}
