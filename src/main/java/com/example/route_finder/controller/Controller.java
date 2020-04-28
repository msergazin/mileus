package com.example.route_finder.controller;

import com.example.route_finder.entity.FastestCarRequest;
import com.example.route_finder.entity.Winner;
import com.example.route_finder.service.RouteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final RouteService routeService;
    @Autowired
    public Controller(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping(
            value = "/find_fastest_car", consumes = "application/json", produces = "application/json")
    public String createPerson(@RequestBody FastestCarRequest fastestCarRequest) throws JsonProcessingException {
//        System.out.println(fastestCarRequest.getDestination());
        Winner winner = routeService.findTheWinnerCar(fastestCarRequest);
        //remove the winner
        //TODO rename intersections to something else
        fastestCarRequest.getWaypoints().remove(winner.getWaypointName());
        routeService.calculateHowMuchTimeOthersWouldNeedToReachTheSameDistanceFromDest(fastestCarRequest, winner);
        return "asd";
    }
}
