package com.andrewlevada.carephoneserver.logic;

import java.sql.Timestamp;

public class LinkRequest {
    public String uid;
    public String code;
    public Timestamp timestamp;

    public LinkRequest(String uid, String code, Timestamp timestamp) {
        this.uid = uid;
        this.code = code;
        this.timestamp = timestamp;
    }
}
