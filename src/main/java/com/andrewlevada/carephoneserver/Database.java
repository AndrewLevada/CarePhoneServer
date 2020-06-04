package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.LogRecord;
import com.andrewlevada.carephoneserver.logic.PhoneNumber;
import com.andrewlevada.carephoneserver.logic.StatisticsPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Component
public class Database {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Users

    public void addUser(String uid) {
        jdbcTemplate.update("INSERT INTO public.\"CaredUsers\" (uid, caretaker_uid, is_whitelist_active) VALUES (?, NULL, TRUE)",
                uid);
    }

    public Boolean hasUser(String uid) {
        List<String> list = jdbcTemplate.query("SELECT uid FROM public.\"CaredUsers\" WHERE uid = ?",
                (resultSet, i) -> resultSet.getString("uid"), uid);

        return list.size() == 1;
    }

    // Whitelist

    public List<PhoneNumber> getWhitelist(String uid) {
        return jdbcTemplate.query("SELECT phone_number, label FROM public.\"WhitelistRecords\" WHERE uid = ?", new PhoneNumber.Mapper(), uid);
    }

    public void addWhitelistRecord(String uid, PhoneNumber phoneNumber) {
        jdbcTemplate.update("INSERT INTO public.\"WhitelistRecords\" (uid, phone_number, label) VALUES (?, ?, ?)",
                uid, phoneNumber.phone, phoneNumber.label);
    }

    public void deleteWhitelistRecord(String uid, String phone) {
        jdbcTemplate.update("DELETE FROM public.\"WhitelistRecords\" WHERE uid = ? AND phone_number = ?",
                uid, phone);
    }

    public void editWhitelistRecord(String uid, String prevPhone, PhoneNumber phoneNumber) {
        jdbcTemplate.update("UPDATE public.\"WhitelistRecords\" SET phone_number = ?, label = ? WHERE uid = ? AND phone_number = ?",
                phoneNumber.phone, phoneNumber.label, uid, prevPhone);
    }

    // Whitelist State

    public Boolean getWhitelistState(String uid) {
        RowMapper<Boolean> mapper = (resultSet, i) -> resultSet.getBoolean("is_whitelist_active");

        List<Boolean> list = jdbcTemplate.query("SELECT is_whitelist_active FROM public.\"CaredUsers\" WHERE uid = ?",
                mapper, uid);

        if (list.size() != 0) return list.get(0);
        else return false;
    }

    public void setWhitelistState(String uid, boolean state) {
        jdbcTemplate.update("UPDATE public.\"CaredUsers\" SET is_whitelist_active = ? WHERE uid = ?",
                state, uid);
    }

    // Log

    public List<LogRecord> getAllLogRecords(String uid) {
        return jdbcTemplate.query("SELECT * FROM public.\"LogRecords\" WHERE uid = ?", new LogRecord.Mapper(), uid);
    }

    public List<LogRecord> getLimitedLogRecords(String uid, int limit, int offset) {
        return jdbcTemplate.query("SELECT * FROM public.\"LogRecords\" WHERE uid = ? LIMIT ? OFFSET ?", new LogRecord.Mapper(), uid, limit, offset);
    }

    public void addLogRecord(String uid, String phoneNumber, Date startTimestamp, int secondsDuration, int type) {
        jdbcTemplate.update("INSERT INTO public.\"LogRecords\" (uid, phone_number, start_timestamp, seconds_duration, type) VALUES (?, ?, ?, ?, ?)",
                uid, phoneNumber, new java.sql.Timestamp(startTimestamp.getTime()), secondsDuration, type);
    }

    // Cared List

    public List<String> getCaredList(String uid) {
        return jdbcTemplate.query("SELECT uid FROM public.\"CaredUsers\" WHERE caretaker_uid = ?", new uidMapper(), uid);
    }

    // Additional Mappers

    private static class uidMapper implements RowMapper<String> {
        @Override
        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString("uid");
        }
    }
}
