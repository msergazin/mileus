package com.example.route_finder.entity;

import com.example.route_finder.entity.osrm.objects.Waypoint;

import java.util.HashMap;

public class Winner {
    private Waypoint waypointName;
    private Double distanceToDestinationAtTimeLimit;
    private HashMap<String, Double> totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit;

    public Winner(Waypoint waypoint, Double distanceToDestinationAtTimeLimit, HashMap<String, Double> totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit) {
        this.waypointName = waypoint;
        this.distanceToDestinationAtTimeLimit = distanceToDestinationAtTimeLimit;
        this.totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit = totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit;
    }

    public HashMap<String, Double> getTotalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit() {
        return totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit;
    }

    public void setTotalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit(HashMap<String, Double> totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit) {
        this.totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit = totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit;
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
