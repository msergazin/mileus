package com.example.route_finder.entity;


import com.example.route_finder.entity.Location;
import com.example.route_finder.entity.Waypoint;

import java.util.ArrayList;

public class FastestCarRequest {

    private Location origin;
    private Location destination;
    private Integer time;
    private ArrayList<Waypoint> waypoints;
    public FastestCarRequest(Location origin, Location destination, Integer time, ArrayList<Waypoint> waypoints){
        this.origin = origin;
        this.destination = destination;
        this.time = time;
        this.waypoints = waypoints;
    }

    public Location getOrigin() {
        return origin;
    }
    public void setOrigin(Location origin) {
        this.origin = origin;
    }
    public Location getDestination() {
        return destination;
    }
    public void setDestination(Location destination) {
        this.destination = destination;
    }
    public Integer getTime() {
        return time;
    }
    public void setTime(Integer time) {
        this.time = time;
    }
    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}

