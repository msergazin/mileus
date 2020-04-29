package com.example.route_finder.service;

import com.example.route_finder.entity.FastestCarRequest;
import com.example.route_finder.entity.Winner;

import java.util.HashMap;

public interface RouteService {
    HashMap<String, Double> calculateDelays(FastestCarRequest fastestCarRequest, Winner winner);
    Winner findTheWinnerCar(FastestCarRequest fastestCarRequest);
}
