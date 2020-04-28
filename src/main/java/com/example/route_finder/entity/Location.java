package com.example.route_finder.entity;

import java.util.ArrayList;
import java.util.Objects;

public class Location {
    private Double lat;
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Location(Double lat, Double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public static Location arrayOfCoordinatesToLocation(ArrayList<Double> array) {
        return new Location(array.get(1), array.get(0));
    }

//    public static Boolean equals()

    /**
     * Calculates the distance in km between two lat/long points
     * using the haversine formula
     */
    public static Double haversine(
            Location loc1, Location loc2) {
        int r = 6371000; // average radius of the earth in km
        double dLat = Math.toRadians(loc2.lat - loc1.lat);
        double dLon = Math.toRadians(loc2.lon - loc1.lon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(loc1.lat)) * Math.cos(Math.toRadians(loc2.lat))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return lat.equals(location.lat) &&
                lon.equals(location.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString() {
        return lon + "," + lat;
    }
}
