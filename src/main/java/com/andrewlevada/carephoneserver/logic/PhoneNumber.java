package com.andrewlevada.carephoneserver.logic;

public class PhoneNumber {
    private String phone;
    private String label;

    public PhoneNumber(String phone, String label) {
        this.phone = phone;
        this.label = label;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
