package com.example.ProfileService;

import javax.validation.constraints.NotNull;

public class AuthServerUser {
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
