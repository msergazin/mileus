package com.example.route_finder.entity.osrm.objects;

import com.example.route_finder.entity.Location;

public class Waypoint extends Location {
    private String name;
    public Waypoint(String name, Double lat, Double lon) {
        super(lat, lon);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "name: " + name +", (lat,lon): " + super.toString();
    }
    public String toLocString() {
        return super.toString();
    }
}