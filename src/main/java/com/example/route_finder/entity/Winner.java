package com.example.route_finder.entity;

public class Winner {
    private Waypoint waypointName;
    private Double distanceToDestinationAtTimeLimit;
    public Winner(Waypoint waypoint, Double distanceToDestinationAtTimeLimit) {
        this.waypointName = waypoint;
        this.distanceToDestinationAtTimeLimit = distanceToDestinationAtTimeLimit;
    }
    public Waypoint getWaypointName() {
        return waypointName;
    }

    public void setWaypointName(Waypoint waypointName) {
        this.waypointName = waypointName;
    }

    public Double getDistanceToDestinationAtTimeLimit() {
        return distanceToDestinationAtTimeLimit;
    }

    public void setDistanceToDestinationAtTimeLimit(Double distanceToDestinationAtTimeLimit) {
        this.distanceToDestinationAtTimeLimit = distanceToDestinationAtTimeLimit;
    }
}
