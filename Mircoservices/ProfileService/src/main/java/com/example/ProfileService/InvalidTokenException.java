package com.example.ProfileService;

public class InvalidTokenException extends Throwable {
    public InvalidTokenException(Long user_id, String token) {
            super("The token : " + token + "for the user : " + user_id + "is invalid.");
    }
}
