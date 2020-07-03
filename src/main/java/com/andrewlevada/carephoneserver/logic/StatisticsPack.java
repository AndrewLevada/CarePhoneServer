package com.andrewlevada.carephoneserver.logic;

import java.util.List;

public class StatisticsPack {
    public List<Integer> periodsMinutes;
    public List<String> phonesLabels;
    public List<Integer> phonesMinutes;

    public StatisticsPack(List<Integer> periodsMinutes, List<String> phonesLabels, List<Integer> phonesMinutes) {
        this.periodsMinutes = periodsMinutes;
        this.phonesLabels = phonesLabels;
        this.phonesMinutes = phonesMinutes;
    }
}
