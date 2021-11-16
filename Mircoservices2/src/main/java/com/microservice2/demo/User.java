package com.microservice2.demo;

import javax.validation.constraints.NotNull;
import java.util.Random;

public class User {
    @NotNull
    private long id;
    @NotNull
    private String mdp;

    public long getId(){
        return this.id;
    }

    public String getMdp(){
        return this.mdp;
    }

    public void setId(long id) {
        this.id=id;
    }

    public void setMdp(String mdp) {
        this.mdp=mdp;
    }

}
