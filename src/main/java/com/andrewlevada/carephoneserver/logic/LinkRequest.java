package com.andrewlevada.carephoneserver.logic;

public class LinkRequest {
    public String uid;
    public String code;
    public long endTime;

    public LinkRequest(String uid, String code, long endTime) {
        this.uid = uid;
        this.code = code;
        this.endTime = endTime;
    }
}
