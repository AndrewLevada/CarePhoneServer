package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.PhoneNumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class Requests {
    @Autowired
    private Database database;

    @RequestMapping(method = RequestMethod.GET, path = "/whitelist", consumes = "application/json")
    public String getWhitelist(@RequestBody JSONObject body) {
        return body.getString("userToken");
        // String uid = Toolbox.getUidFromFirebaseAuthToken(body.getString("userToken"));
        // if (uid != null) return database.getWhitelist(uid);
        // else return null;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/whitelist")
    public void putWhitelist(@PathVariable String userToken, @PathVariable String phone, @PathVariable String label) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid != null) database.addWhitelistRecord(uid, new PhoneNumber(phone, label));
    }
}
