package com.andrewlevada.carephoneserver.logic;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneNumber {
    public String phone;
    public String label;

    public PhoneNumber(String phone, String label) {
        this.phone = phone;
        this.label = label;
    }

    public static class Mapper implements RowMapper<PhoneNumber> {
        @Override
        public PhoneNumber mapRow(ResultSet resultSet, int i) throws SQLException {
            return new PhoneNumber(resultSet.getString("phone_number"), resultSet.getString("label"));
        }
    }
}
