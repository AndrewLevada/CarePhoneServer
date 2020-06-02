package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Database {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PhoneNumber> getWhitelist(String uid) {
        return jdbcTemplate.query("SELECT phone_number, label FROM public.\"WhitelistRecords\" WHERE uid=?", new Mapper(), uid);
    }

    public void addWhitelistRecord(String uid, PhoneNumber phoneNumber) {
        jdbcTemplate.update("INSERT INTO public.\"WhitelistRecords\" (uid, phone_number, label) VALUES (?, ?, ?)",
                uid, phoneNumber.getPhone(), phoneNumber.getLabel());
    }
}