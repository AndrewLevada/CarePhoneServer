package com.andrewlevada.carephoneserver.logic;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogRecord {
    public String phoneNumber;
    public long startTimestamp;
    public int secondsDuration;
    public int type;

    public LogRecord(String phoneNumber, long startTimestamp, int secondsDuration, int type) {
        this.phoneNumber = phoneNumber;
        this.startTimestamp = startTimestamp;
        this.secondsDuration = secondsDuration;
        this.type = type;
    }

    public static class Mapper implements RowMapper<LogRecord> {
        @Override
        public LogRecord mapRow(ResultSet resultSet, int i) throws SQLException {
            return new LogRecord(resultSet.getString("phone_number"), resultSet.getLong("start_timestamp"),
                    resultSet.getInt("seconds_duration"), resultSet.getInt("type"));
        }
    }
}
