package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.LogRecord;
import com.andrewlevada.carephoneserver.logic.StatisticsPack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataProcessing {
    public static String generateLinkCode() {
        String code = "" + ThreadLocalRandom.current().nextInt(100000, 999999);
        return code;
    }
}
