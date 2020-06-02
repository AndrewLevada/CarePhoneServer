package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class Requests {
    @Autowired
    private Repository repository;

    @RequestMapping(path="/whitelist/{userToken}")
    public List<PhoneNumber> getCared(@PathVariable String userToken) {
        List<PhoneNumber> array = new ArrayList<>();
        array.add(new PhoneNumber("+7894", "Person"));
        array.add(new PhoneNumber("+78740224", "Hello"));
        array.add(new PhoneNumber("+799999894", "Third"));
        return array;
    }
}
