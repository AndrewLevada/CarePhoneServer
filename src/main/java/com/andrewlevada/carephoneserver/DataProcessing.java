package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.LogRecord;
import com.andrewlevada.carephoneserver.logic.StatisticsPack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataProcessing {
    public static StatisticsPack makeStatisticsPackFromLog(List<LogRecord> log) {
        List<Integer> periodsHours = new ArrayList<>();
        List<String> phonesLabels = new ArrayList<>();
        List<Integer> phonesHours = new ArrayList<>();

        // TODO: Add processing

        return new StatisticsPack(periodsHours, phonesLabels, phonesHours);
    }

    public static String generateLinkCode() {
        String code = "" + ThreadLocalRandom.current().nextInt(100000, 999999);
        return code;
    }
}
