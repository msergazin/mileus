package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteObject {
    private Double distance;
    private Double duration;
    private ArrayList<RouteLeg> legs;
    @JsonCreator
    public RouteObject(
            @JsonProperty("distance") Double distance,
            @JsonProperty("duration") Double duration,
            @JsonProperty("legs") ArrayList<RouteLeg> legs) {
        this.distance = distance;
        this.duration = duration;
        this.legs = legs;
    }
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public ArrayList<RouteLeg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<RouteLeg> legs) {
        this.legs = legs;
    }
}
