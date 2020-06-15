package com.andrewlevada.carephoneserver;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final String serviceAccountKeyPath = "firebase-service-account.json";
    public static final String firebaseDatabaseURL = "https://carephone-app.firebaseio.com";

    public static final List<Long> statisticsPeriods = new ArrayList<>();
    public static final int statisticsTopPhonesAmount = 4;

    static {
        Long dayTime = 1000 * 60 * 60 * 24L;

        statisticsPeriods.add(null);
        statisticsPeriods.add(dayTime * 30);
        statisticsPeriods.add(dayTime * 7);
        statisticsPeriods.add(dayTime);
    }
}
