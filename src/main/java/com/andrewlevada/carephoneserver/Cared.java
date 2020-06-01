package com.andrewlevada.carephoneserver;

public class Cared {
    public String token;
    public String caretakerToken;

    public Cared(String token, String caretakerToken) {
        this.token = token;
        this.caretakerToken = caretakerToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCaretakerToken() {
        return caretakerToken;
    }

    public void setCaretakerToken(String caretakerToken) {
        this.caretakerToken = caretakerToken;
    }
}
