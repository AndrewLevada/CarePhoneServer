package com.andrewlevada.carephoneserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Repository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
}
