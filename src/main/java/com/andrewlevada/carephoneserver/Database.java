package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public void addLogRecord(String uid, String phoneNumber, long startTimestamp, int secondsDuration, int type) {
        jdbcTemplate.update("INSERT INTO public.\"LogRecords\" (uid, phone_number, start_timestamp, seconds_duration, type) VALUES (?, ?, ?, ?, ?)",
                uid, phoneNumber, startTimestamp, secondsDuration, type);
    }

    // Cared List

    public List<CaredUser> getCaredList(String uid) {
        return jdbcTemplate.query("SELECT uid FROM public.\"CaredUsers\" WHERE caretaker_uid = ?",
                new CaredUser.Mapper(), uid);
    }

    // Linking

    public String addLinkRequest(String uid) {
        List<LinkRequest> list = jdbcTemplate.query("SELECT * FROM public.\"LinkRequests\" WHERE uid = ?",
                (resultSet, i) -> new LinkRequest(resultSet.getString("uid"), resultSet.getString("code"), resultSet.getTimestamp("end_timestamp")), uid);

        if (list.size() != 0) {
            if (list.get(0).timestamp.after(new Date(System.currentTimeMillis()))) return list.get(0).code;
            else {
                deleteLinkRequest(uid);
                return addLinkRequest(uid);
            }
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 1000 * 60 * 10);
        String code = DataProcessing.generateLinkCode();
        jdbcTemplate.update("INSERT INTO public.\"LinkRequests\" (uid, code, end_timestamp) VALUES (?, ?, ?)",
                uid, code, timestamp);
        return code;
    }

    public void deleteLinkRequest(String uid) {
        jdbcTemplate.update("DELETE FROM public.\"LinkRequests\" WHERE uid = ?", uid);
    }

    public int tryToLinkCaretaker(String uid, String code) {
        List<LinkRequest> list = jdbcTemplate.query("SELECT * FROM public.\"LinkRequests\" WHERE code = ?",
                (resultSet, i) -> new LinkRequest(resultSet.getString("uid"), resultSet.getString("code"), resultSet.getTimestamp("end_timestamp")), code);

        if (list.size() != 1) return 0;
        LinkRequest linkRequest = list.get(0);

        if (linkRequest.timestamp.after(new Date(System.currentTimeMillis()))) {
            jdbcTemplate.update("UPDATE public.\"CaredUsers\" SET caretaker_uid = ? WHERE uid = ?",
                    uid, linkRequest.uid);
            return 1;
        }

        jdbcTemplate.update("DELETE FROM public.\"LinkRequests\" WHERE uid = ?", linkRequest.uid);
        return 0;
    }

    // Remote

    public boolean checkRemote(String uid, String rUid) {
        List<CaredUser> list = jdbcTemplate.query("SELECT * FROM public.\"CaredUsers\" WHERE uid = ? AND caretaker_uid = ?", new CaredUser.Mapper(),
                rUid, uid);

        return list.size() != 0;
    }
}
