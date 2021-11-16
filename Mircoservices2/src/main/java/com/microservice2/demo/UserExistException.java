package com.microservice2.demo;

public class UserExistException extends RuntimeException {
    public UserExistException(long id){
        super("User already exist : " + id);
    }
}
