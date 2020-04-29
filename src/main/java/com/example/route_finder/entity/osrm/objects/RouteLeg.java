package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteLeg {
    private Annotation annotation;
    private Double distance;
    private Double duration;
    private ArrayList<RouteStep> steps;

    @JsonCreator
    public RouteLeg(
            @JsonProperty("annotation") Annotation annotation,
            @JsonProperty("distance") Double distance,
            @JsonProperty("duration") Double duration,
            @JsonProperty("steps") ArrayList<RouteStep> steps) {
        this.annotation = annotation;
        this.distance = distance;
        this.duration = duration;
        this.steps = steps;
    }
    public ArrayList<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RouteStep> steps) {
        this.steps = steps;
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

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
