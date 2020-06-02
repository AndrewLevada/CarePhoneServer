package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.PhoneNumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class Requests {
    @Autowired
    private Database database;

    @RequestMapping(method = RequestMethod.GET, path = "/whitelist/{userToken}")
    public List<PhoneNumber> getWhitelist(@PathVariable String userToken) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(userToken);
        } catch (FirebaseAuthException e) {
            return null;
        }

        String uid = decodedToken.getUid();
        List<PhoneNumber> array = database.getWhitelist(uid);
        return array;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/whitelist/{userToken}/{phone}/{label}")
    public void putWhitelist(@PathVariable String userToken, @PathVariable String phone, @PathVariable String label) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid != null) database.addWhitelistRecord(uid, new PhoneNumber(phone, label));
    }
}
