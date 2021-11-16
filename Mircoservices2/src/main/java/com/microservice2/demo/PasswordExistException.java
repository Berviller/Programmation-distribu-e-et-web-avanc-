package com.microservice2.demo;

import javax.validation.Valid;

public class PasswordExistException extends RuntimeException {
    public PasswordExistException(String mdp){
        super("Wrong Password");
    }
}
