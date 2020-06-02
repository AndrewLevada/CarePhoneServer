package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.PhoneNumber;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper implements RowMapper<PhoneNumber> {
    @Override
    public PhoneNumber mapRow(ResultSet resultSet, int i) throws SQLException {
        return new PhoneNumber(resultSet.getString("phone_number"), resultSet.getString("label"));
    }
}
