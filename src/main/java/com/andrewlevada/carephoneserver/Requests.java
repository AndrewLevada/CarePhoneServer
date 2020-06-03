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
    public List<PhoneNumber> getWhitelist(@RequestBody String bodyString) {
        JSONObject body = new JSONObject(bodyString);
        String uid = Toolbox.getUidFromFirebaseAuthToken(body);
        if (uid != null) return database.getWhitelist(uid);
        else return null;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/whitelist", consumes = "application/json")
    public void putWhitelist(@RequestBody String bodyString) {
        JSONObject body = new JSONObject(bodyString);
        String uid = Toolbox.getUidFromFirebaseAuthToken(body);
        if (uid != null) database.addWhitelistRecord(uid, new PhoneNumber(body.getString("phone_number"), body.getString("label")));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/whitelist", consumes = "application/json")
    public void postWhitelist(@RequestBody String bodyString) {
        JSONObject body = new JSONObject(bodyString);
        String uid = Toolbox.getUidFromFirebaseAuthToken(body);
        if (uid != null) database.editWhitelistRecord(uid, body.getString("prev_phone"), new PhoneNumber(body.getString("phone_number"), body.getString("label")));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/whitelist", consumes = "application/json")
    public void deleteWhitelist(@RequestBody String bodyString) {
        JSONObject body = new JSONObject(bodyString);
        String uid = Toolbox.getUidFromFirebaseAuthToken(body);
        if (uid != null) database.deleteWhitelistRecord(uid, body.getString("phone_number"));
    }
}
