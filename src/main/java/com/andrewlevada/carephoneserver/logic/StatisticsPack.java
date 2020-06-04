package com.andrewlevada.carephoneserver.logic;

import java.util.List;

public class StatisticsPack {
    public List<Integer> periodsHours;
    public List<String> phonesLabels;
    public List<Integer> phonesHours;

    public StatisticsPack(List<Integer> periodsHours, List<String> phonesLabels, List<Integer> phonesHours) {
        this.periodsHours = periodsHours;
        this.phonesLabels = phonesLabels;
        this.phonesHours = phonesHours;
    }
}
