package com.example.route_finder.entity;

public class DistanceAndDuration {
    private Double distanceToOrigin;
    private Double durationSoFar;
    public DistanceAndDuration(Double distanceToOrigin, Double durationSoFar) {
        this.distanceToOrigin = distanceToOrigin;
        this.durationSoFar = durationSoFar;
    }

    public Double getDistanceToOrigin() {
        return distanceToOrigin;
    }

    public void setDistanceToOrigin(Double distanceToOrigin) {
        this.distanceToOrigin = distanceToOrigin;
    }

    public Double getDurationSoFar() {
        return durationSoFar;
    }

    public void setDurationSoFar(Double durationSoFar) {
        this.durationSoFar = durationSoFar;
    }
}
