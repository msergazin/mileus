package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteStep {
    private Double distance;
    private Double duration;
    private Geometry geometry;

    @JsonCreator
    public RouteStep(
            @JsonProperty("distance") Double distance,
            @JsonProperty("duration") Double duration,
            @JsonProperty("geometry") Geometry geometry
    ) {
        this.distance = distance;
        this.duration = duration;
        this.geometry = geometry;
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

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
