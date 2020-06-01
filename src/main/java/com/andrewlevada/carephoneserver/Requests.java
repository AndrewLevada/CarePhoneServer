package com.andrewlevada.carephoneserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Requests {
    @Autowired
    private Repository repository;

    @RequestMapping(path="/getCared/{token}")
    public String getCared(@PathVariable String token) {
        return "test";
    }
}
