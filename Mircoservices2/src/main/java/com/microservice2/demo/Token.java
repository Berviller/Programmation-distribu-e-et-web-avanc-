package com.microservice2.demo;

import javax.validation.constraints.NotNull;
import java.util.Random;

public class Token {
    private String tk;

    public Token() {
        this.tk = getRandomString();
    }

    public void setToken(String tk) {
        this.tk=tk;
    }

    public String getToken(){
        return this.tk;
    }

    protected static String getRandomString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randstring = new StringBuilder();
        Random rnd = new Random();
        while (randstring.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            randstring.append(chars.charAt(index));
        }
        String randStr = randstring.toString();
        return randStr;
    }
}
