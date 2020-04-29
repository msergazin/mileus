package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape= JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({ "lat", "lon" })
public class ArrayLocation {
    private Double lat;
    private Double lon;

    @JsonCreator
    public ArrayLocation(
            Double lat, Double lon
    ) {
        this.lat = lat;
        this.lon = lon;
    }

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
}
