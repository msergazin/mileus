package com.example.route_finder.controller;

import com.example.route_finder.entity.FastestCarRequest;
import com.example.route_finder.entity.Response;
import com.example.route_finder.entity.Winner;
import com.example.route_finder.service.RouteServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class Controller {

    private final RouteServiceImpl routeService;
    @Autowired
    public Controller(RouteServiceImpl routeService) {
        this.routeService = routeService;
    }

    @PostMapping(
            value = "/find_fastest_car", consumes = "application/json", produces = "application/json")
    public Response fintheWinnerCarAndDelays(
            @RequestBody FastestCarRequest fastestCarRequest) {
        Winner winner = routeService.findTheWinnerCar(fastestCarRequest);
        //remove the winner
        //TODO interface and implementations
        //TODO rename intersections to something else
        fastestCarRequest.getWaypoints().remove(winner.getWaypointName());
        HashMap<String, Double> delays = routeService.calculateDelays(fastestCarRequest, winner);

        return new Response(winner.getWaypointName().getName(), delays);
    }
}
