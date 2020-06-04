package com.andrewlevada.carephoneserver.logic;

import java.sql.Timestamp;

public class LinkRequest {
    public String uid;
    public Timestamp timestamp;

    public LinkRequest(String uid, Timestamp timestamp) {
        this.uid = uid;
        this.timestamp = timestamp;
    }
}
