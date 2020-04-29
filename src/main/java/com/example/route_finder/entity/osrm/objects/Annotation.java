package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Annotation {
    @JsonCreator
    public Annotation(
            @JsonProperty("distance") ArrayList<Double> distance,
            @JsonProperty("duration") ArrayList<Double> duration,
            @JsonProperty("speed") ArrayList<Double> speed) {
        this.distance = distance;
        this.duration = duration;
        this.speed = speed;
    }
    private ArrayList<Double> distance;
    private ArrayList<Double> duration;
    private ArrayList<Double> speed;

    public ArrayList<Double> getDistance() {
        return distance;
    }

    public void setDistance(ArrayList<Double> distance) {
        this.distance = distance;
    }

    public ArrayList<Double> getDuration() {
        return duration;
    }

    public void setDuration(ArrayList<Double> duration) {
        this.duration = duration;
    }

    public ArrayList<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(ArrayList<Double> speed) {
        this.speed = speed;
    }
}
