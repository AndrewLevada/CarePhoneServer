package com.andrewlevada.carephoneserver.logic;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class LogRecord {
    public String phoneNumber;
    public Date startTimestamp;
    public int secondsDuration;
    public int type;

    public LogRecord(String phoneNumber, Date startTimestamp, int secondsDuration, int type) {
        this.phoneNumber = phoneNumber;
        this.startTimestamp = startTimestamp;
        this.secondsDuration = secondsDuration;
        this.type = type;
    }

    public static class Mapper implements RowMapper<LogRecord> {
        @Override
        public LogRecord mapRow(ResultSet resultSet, int i) throws SQLException {
            Date processedDate = new Date(resultSet.getDate("start_timestamp").getTime());

            return new LogRecord(resultSet.getString("phone_number"), processedDate,
                    resultSet.getInt("seconds_duration"), resultSet.getInt("type"));
        }
    }
}
