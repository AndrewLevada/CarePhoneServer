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
        return jdbcTemplate.query("select phone_number, label from WhitelistRecords where uid=?", new Mapper(), uid);
    }
}
