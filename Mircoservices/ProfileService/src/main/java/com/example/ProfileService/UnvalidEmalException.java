package com.example.ProfileService;

public class UnvalidEmalException extends RuntimeException {
    public UnvalidEmalException(String email){
        super("Unvalid Email adress : " + email);
    }
}
