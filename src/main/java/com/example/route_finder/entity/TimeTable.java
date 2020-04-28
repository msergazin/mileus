package com.example.route_finder.entity;

import java.util.ArrayList;

public class TimeTable {
    private ArrayList<ArrayList<Double>> durations;

    public ArrayList<ArrayList<Double>> getDurations() {
        return durations;
    }

    public void setDurations(ArrayList<ArrayList<Double>> durations) {
        this.durations = durations;
    }
}
