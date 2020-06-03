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

    @RequestMapping(method = RequestMethod.GET, path = "/whitelist")
    public List<PhoneNumber> getWhitelist(@RequestParam String userToken) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid != null) return database.getWhitelist(uid);
        else return null;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/whitelist")
    public void putWhitelist(@RequestParam String userToken, @RequestParam String phone, @RequestParam String label) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid != null) database.addWhitelistRecord(uid, new PhoneNumber(phone, label));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/whitelist")
    public void postWhitelist(@RequestParam String userToken, @RequestParam String prevPhone, @RequestParam String phone, @RequestParam String label) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid != null) database.editWhitelistRecord(uid, prevPhone, new PhoneNumber(phone, label));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/whitelist")
    public void deleteWhitelist(@RequestParam String userToken, @RequestParam String phone) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid != null) database.deleteWhitelistRecord(uid, phone);
    }
}
