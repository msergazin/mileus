package com.example.route_finder.controller;

import com.example.route_finder.entity.FastestCarRequest;
import com.example.route_finder.entity.Response;
import com.example.route_finder.entity.Winner;
import com.example.route_finder.service.MockPasswordService;
import com.example.route_finder.service.impl.RouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
public class Controller {

    private final RouteServiceImpl routeService;
    private final MockPasswordService mockPasswordService;
    @Autowired
    public Controller(
            RouteServiceImpl routeService,
            MockPasswordService mockPasswordService) {
        this.routeService = routeService;
        this.mockPasswordService = mockPasswordService;
    }

    @PostMapping(
            value = "/find_fastest_car", consumes = "application/json", produces = "application/json")
    public Response fintheWinnerCarAndDelays(
            @RequestHeader("x-secret") String xSecret,
            @RequestBody @Valid FastestCarRequest fastestCarRequest) {
        if (mockPasswordService.checkPass(xSecret)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return routeService.findTheWinnerAndCalculateDelays(fastestCarRequest);
    }
}
