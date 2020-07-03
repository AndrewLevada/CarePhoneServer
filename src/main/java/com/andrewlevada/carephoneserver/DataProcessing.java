package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.Pair;
import com.andrewlevada.carephoneserver.logic.StatisticsPack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataProcessing {
    private static final int LOOP_LIMIT = 100;

    public static String generateLinkCode(Database database) {
        String code;

        for (int i = 0; i < LOOP_LIMIT; i++) {
            code = "" + ThreadLocalRandom.current().nextInt(100000, 999999);
            if (database.checkIfLinkCodeValid(code)) return code;
        }

        return "000000";
    }

    public static StatisticsPack generateStatisticsPack(Database database, String uid) {
        Pair<List<String>, List<Integer>> phonesData = database.getTopPhonesByMinutes(uid, Config.statisticsTopPhonesAmount);

        List<Integer> periodsData = new ArrayList<>();
        for (Long period: Config.statisticsPeriods)
            periodsData.add(period != null ? database.getTalkMinutesByPeriod(uid, period) : database.getTalkMinutes(uid));

        return new StatisticsPack(periodsData, phonesData.first, phonesData.second);
    }
}
