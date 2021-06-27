package net.zenebris.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class Token   {
    @JsonProperty("token")
    private String token = null;

    /**
    **/
    public Token token(String token) {
        this.token = token;
        return this;
    }

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
