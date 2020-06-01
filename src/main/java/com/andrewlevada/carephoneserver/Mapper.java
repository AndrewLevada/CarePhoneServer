package com.andrewlevada.carephoneserver;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper implements RowMapper<Cared> {
    @Override
    public Cared mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Cared(resultSet.getString("token"), resultSet.getString("caretaker_token"));
    }
}
