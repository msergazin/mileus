package com.example.route_finder.entity.osrm.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteResponse {
    private String code;
    private ArrayList<RouteObject> routes;
    private Double timeSpent;

    @JsonCreator
    public RouteResponse(
            @JsonProperty("routes") ArrayList<RouteObject> routes,
            @JsonProperty("code") String code) {
        this.routes = routes;
        this.code = code;
    }

    public Double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public ArrayList<RouteObject> getRoutes() {
        return routes;
    }
    public RouteObject getTheRoute() {
        return routes.get(0);
    }

    public void setRoutes(ArrayList<RouteObject> routes) {
        this.routes = routes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
