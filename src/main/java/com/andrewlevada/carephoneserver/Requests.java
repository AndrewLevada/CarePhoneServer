package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class Requests {
    @Autowired
    private Database database;

    // Users

    @RequestMapping(method = RequestMethod.PUT, path = "/users")
    public void tryToPutUser(@RequestParam String userToken) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return;
        if (!database.hasUser(uid)) database.addUser(uid);
    }
    
    // Whitelist

    @RequestMapping(method = RequestMethod.GET, path = "/whitelist")
    public List<PhoneNumber> getWhitelist(@RequestParam String userToken) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return null;
        return database.getWhitelist(uid);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/whitelist")
    public void putWhitelist(@RequestParam String userToken, @RequestParam String phone, @RequestParam String label) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return;
        database.addWhitelistRecord(uid, new PhoneNumber(phone, label));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/whitelist")
    public void postWhitelist(@RequestParam String userToken, @RequestParam String prevPhone, @RequestParam String phone, @RequestParam String label) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return;
        database.editWhitelistRecord(uid, prevPhone, new PhoneNumber(phone, label));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/whitelist")
    public void deleteWhitelist(@RequestParam String userToken, @RequestParam String phone) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return;
        database.deleteWhitelistRecord(uid, phone);
    }

    // Whitelist State

    @RequestMapping(method = RequestMethod.GET, path = "/whitelist/state")
    public Boolean getWhitelistState(@RequestParam String userToken) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return null;
        return database.getWhitelistState(uid);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/whitelist/state")
    public void postWhitelistState(@RequestParam String userToken, @RequestParam Boolean state) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return;
        database.setWhitelistState(uid, state);
    }

    // Statistics

    @RequestMapping(method = RequestMethod.GET, path = "/statistics")
    public StatisticsPack getStatisticsPack(@RequestParam String userToken) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return null;
        return DataProcessing.makeStatisticsPackFromLog(database.getAllLogRecords(uid));
    }

    // Log

    @RequestMapping(method = RequestMethod.GET, path = "/log")
    public List<LogRecord> getLog(@RequestParam String userToken, @RequestParam int limit, @RequestParam int offset) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return null;
        return database.getLimitedLogRecords(uid, limit, offset);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/log")
    public void putLog(@RequestParam String userToken, @RequestParam String phoneNumber, @RequestParam Date startTimestamp, @RequestParam int secondsDuration, @RequestParam int type) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return;
        database.addLogRecord(uid, phoneNumber, startTimestamp, secondsDuration, type);
    }

    // Cared List

    @RequestMapping(method = RequestMethod.GET, path = "/caredList")
    public List<String> getCaredList(@RequestParam String userToken) {
        String uid = Toolbox.getUidFromFirebaseAuthToken(userToken);
        if (uid == null) return null;
        return database.getCaredList(uid);
    }
}
