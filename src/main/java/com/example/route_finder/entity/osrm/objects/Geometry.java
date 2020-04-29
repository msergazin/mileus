package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    private ArrayList<ArrayList<Double>> coordinates;

    @JsonCreator
    public Geometry(
            @JsonProperty("coordinates") ArrayList<ArrayList<Double>> coordinates
    ) {
        this.coordinates = coordinates;
    }
    public ArrayList<ArrayList<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<ArrayList<Double>> coordinates) {
        this.coordinates = coordinates;
    }
}
