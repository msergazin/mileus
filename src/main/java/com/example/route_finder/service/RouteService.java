package com.example.route_finder.service;

import com.example.route_finder.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class RouteService {
    private final RestTemplate restTemplate;
    final String tableUri = "http://router.project-osrm.org/table/v1/driving/";
    final String routeUri = "http://router.project-osrm.org/route/v1/driving/";

    @Autowired
    public RouteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void calculateHowMuchTimeOthersWouldNeedToReachTheSameDistanceFromDest(FastestCarRequest fastestCarRequest, Winner winner) {
        for (int waypointOrCarIndex = 0; waypointOrCarIndex < fastestCarRequest.getWaypoints().size(); waypointOrCarIndex++) {
            RouteResponse routeResponse = getRouteResponseFromOSRM(fastestCarRequest, waypointOrCarIndex);

            int indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon = Integer.MAX_VALUE;
            ArrayList<Double> legDurations = null;
            ArrayList<Location> uniqueIntersections = null;

            for (int legIndex = routeResponse.getTheRoute().getLegs().size() - 1; legIndex >= 0 ; legIndex--) {
                uniqueIntersections = getUniqueIntersectionsFromGeometryCoordinatesOfThisLeg(routeResponse, legIndex);
                legDurations = routeResponse.getTheRoute().getLegs().get(legIndex).getAnnotation().getDuration();
                for (int uniqueIntersectionIndex = uniqueIntersections.size() - 1; uniqueIntersectionIndex >= 0; uniqueIntersectionIndex--) {
                    Double distanceToDest = Location.haversine(fastestCarRequest.getDestination(), uniqueIntersections.get(uniqueIntersectionIndex));
                    if (distanceToDest > winner.getDistanceToDestinationAtTimeLimit() || isWithin5Meters(winner, distanceToDest)) {
                        System.out.println("this is where the car is at the same distance as the winner car when it won: " + uniqueIntersections.get(uniqueIntersectionIndex));
                        System.out.println(winner.getDistanceToDestinationAtTimeLimit());
                        System.out.println("distanceToDest: " + distanceToDest);
                        //save last point where car was within time limit
                        indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon = uniqueIntersectionIndex;
                        //break
                        uniqueIntersectionIndex = -1;
                        legIndex = -1;
                    }
                }
            }
            Double delay = calculateDelay(indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon, legDurations, uniqueIntersections);
        }

    }

    private Double calculateDelay(int indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon, ArrayList<Double> legDurations, ArrayList<Location> uniqueIntersections) {
        Double delay = 0.0;
        //# of durations in leg = (# of unique intersections - 1)
        for (int i = indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon; i < uniqueIntersections.size() - 1; i++) {
            delay += legDurations.get(i);
        }
        System.out.println("delay: " + delay);
        return delay;
    }

    private boolean isWithin5Meters(Winner winner, Double distanceToDest) {
        return Math.abs(distanceToDest - winner.getDistanceToDestinationAtTimeLimit()) < 5;
    }

    //TODO could also snap the location
    public Winner findTheWinnerCar(FastestCarRequest fastestCarRequest) throws JsonProcessingException {
        Double closestDistanceToDest = Double.MAX_VALUE;
        int indexOfaWinner = 99;
        for (int waypointOrCarIndex = 0; waypointOrCarIndex < fastestCarRequest.getWaypoints().size(); waypointOrCarIndex++) {
            RouteResponse routeResponse = getRouteResponseFromOSRM(fastestCarRequest, waypointOrCarIndex);
//            RouteResponse routeResponse = mockResponse();
            Double distanceToDest = findHowFarTheCarIsFromTheOriginByTimeLimitByAir(routeResponse, fastestCarRequest);
            System.out.println("WayPoint: " + fastestCarRequest.getWaypoints().get(waypointOrCarIndex));
            if (distanceToDest < closestDistanceToDest) {
                closestDistanceToDest = distanceToDest;
                indexOfaWinner = waypointOrCarIndex;
            }
        }
        System.out.println("closest car at the time limit is : " + fastestCarRequest.getWaypoints().get(indexOfaWinner));
        System.out.println("distance to dest at the time limit: " + closestDistanceToDest);
        return new Winner(fastestCarRequest.getWaypoints().get(indexOfaWinner), closestDistanceToDest);
    }

    private RouteResponse getRouteResponseFromOSRM(FastestCarRequest fastestCarRequest, int waypointOrCarIndex) {
        String uri = routeUri + fastestCarRequest.getOrigin() + ";"
                + fastestCarRequest.getWaypoints().get(waypointOrCarIndex).toLocString() + ";"
                + fastestCarRequest.getDestination()
                + "?steps=true&annotations=speed,distance,duration&geometries=geojson";
        return restTemplate.getForObject(uri, RouteResponse.class);
    }

    private RouteResponse mockResponse() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = mockJson();
        RouteResponse routeResponse = objectMapper.readValue(json, RouteResponse.class);
        return routeResponse;
    }


    private Double findHowFarTheCarIsFromTheOriginByTimeLimitByAir(RouteResponse routeResponse, FastestCarRequest fastestCarRequest) {
        int legIndexStop = 0;
        int lastPointWhereDurationDidNotExceedTimeLimit = 0;
        Double durationVar = 0.0;
        ArrayList<Double> legDurations = null;
        //traverse steps of the legs of the route
        for (int legIndex = 0; legIndex < routeResponse.getTheRoute().getLegs().size(); legIndex++) {
            legDurations = routeResponse.getTheRoute().getLegs().get(legIndex).getAnnotation().getDuration();
            for (int durationsIndex = 0; durationsIndex < legDurations.size(); durationsIndex++) {
                if (durationVar + legDurations.get(durationsIndex) <= fastestCarRequest.getTime()) {
                    durationVar += legDurations.get(durationsIndex);
                } else {
                    //save last point where car was within time limit
                    lastPointWhereDurationDidNotExceedTimeLimit = durationsIndex;
                    legIndexStop = legIndex;
                    //break
                    durationsIndex = legDurations.size();
                    legIndex = routeResponse.getTheRoute().getLegs().size();
                }
            }
        }
        ArrayList<Location> uniqueIntersections = getUniqueIntersectionsFromGeometryCoordinatesOfThisLeg(routeResponse, legIndexStop);
        //TODO can interpolate here to get more precise location
        Location lastKnownLocationWithinTimeLimit = uniqueIntersections.get(lastPointWhereDurationDidNotExceedTimeLimit + 1);
        return Location.haversine(fastestCarRequest.getDestination(), lastKnownLocationWithinTimeLimit);
    }

    //unite all the geometry coordinates intersection into one array
    private ArrayList<Location> getUniqueIntersectionsFromGeometryCoordinatesOfThisLeg(RouteResponse routeResponse, int legIndexStop) {
        Set uniqueIntersections = new LinkedHashSet<Location>();
        for (int step = 0; step < routeResponse.getTheRoute().getLegs().get(legIndexStop).getSteps().size(); step++) {

            if (routeResponse.getTheRoute().getLegs().get(legIndexStop).getSteps().get(step).getDistance() != 0 )
            routeResponse.getTheRoute().getLegs().get(legIndexStop).getSteps().get(step).getGeometry().getCoordinates()
                    .forEach(
                            intersectionCoordinates -> {
//                                System.out.println(Location.arrayOfCoordinatesToLocation(intersectionCoordinates));
                                uniqueIntersections.add(Location.arrayOfCoordinatesToLocation(intersectionCoordinates));
                            }
                    );
        }
//        System.out.println("unique intersections:");
//        uniqueIntersections.forEach(
//                c -> System.out.println(c)
//        );
        return new ArrayList<Location> (uniqueIntersections);
    }

    private String mockJson() {
        return "{\n" +
                "    \"code\": \"Ok\",\n" +
                "    \"routes\": [\n" +
                "        {\n" +
                "            \"distance\": 686,\n" +
                "            \"duration\": 102.3,\n" +
                "            \"geometry\": {\n" +
                "                \"coordinates\": [\n" +
                "                    [\n" +
                "                        14.446652,\n" +
                "                        50.084837\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.448818,\n" +
                "                        50.084596\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.453412,\n" +
                "                        50.08401\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.4536,\n" +
                "                        50.083915\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.454997,\n" +
                "                        50.082728\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.455121,\n" +
                "                        50.082659\n" +
                "                    ]\n" +
                "                ],\n" +
                "                \"type\": \"LineString\"\n" +
                "            },\n" +
                "            \"legs\": [\n" +
                "                {\n" +
                "                    \"annotation\": {\n" +
                "                        \"distance\": [\n" +
                "                            8.544731605918217,\n" +
                "                            25.845252030011714,\n" +
                "                            28.59349210616636,\n" +
                "                            93.90880459336064,\n" +
                "                            94.66919192017058,\n" +
                "                            19.86891596909933,\n" +
                "                            21.802721449194088,\n" +
                "                            12.846507611173664,\n" +
                "                            20.822390000599597,\n" +
                "                            13.638765287872276,\n" +
                "                            2.6075372289539223,\n" +
                "                            3.9318117425590127,\n" +
                "                            80.79173288923869,\n" +
                "                            58.02392734333691,\n" +
                "                            5.382120519175597\n" +
                "                        ],\n" +
                "                        \"duration\": [\n" +
                "                            1,\n" +
                "                            3.3,\n" +
                "                            3.4,\n" +
                "                            12.1,\n" +
                "                            11.4,\n" +
                "                            2.6,\n" +
                "                            2.8,\n" +
                "                            2.4,\n" +
                "                            3.9,\n" +
                "                            2.6,\n" +
                "                            0.5,\n" +
                "                            0.7,\n" +
                "                            15.3,\n" +
                "                            11,\n" +
                "                            1.1\n" +
                "                        ],\n" +
                "                        \"speed\": [\n" +
                "                            8.5,\n" +
                "                            7.8,\n" +
                "                            8.4,\n" +
                "                            7.8,\n" +
                "                            8.3,\n" +
                "                            7.6,\n" +
                "                            7.8,\n" +
                "                            5.4,\n" +
                "                            5.3,\n" +
                "                            5.2,\n" +
                "                            5.2,\n" +
                "                            5.6,\n" +
                "                            5.3,\n" +
                "                            5.3,\n" +
                "                            4.9\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"distance\": 491.3,\n" +
                "                    \"duration\": 74.1,\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"distance\": 491.3,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 74.1,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.446652,\n" +
                "                                        50.084837\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.44677,\n" +
                "                                        50.084824\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.447127,\n" +
                "                                        50.084785\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.447522,\n" +
                "                                        50.084742\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.448818,\n" +
                "                                        50.084596\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450119,\n" +
                "                                        50.08443\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450392,\n" +
                "                                        50.084395\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450692,\n" +
                "                                        50.084358\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450869,\n" +
                "                                        50.084337\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451155,\n" +
                "                                        50.0843\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451343,\n" +
                "                                        50.084278\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451379,\n" +
                "                                        50.084274\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451433,\n" +
                "                                        50.084267\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.452545,\n" +
                "                                        50.084131\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.453342,\n" +
                "                                        50.084028\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.453412,\n" +
                "                                        50.08401\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        100\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.446652,\n" +
                "                                        50.084837\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        45,\n" +
                "                                        105,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.447127,\n" +
                "                                        50.084785\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        105,\n" +
                "                                        165,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.447522,\n" +
                "                                        50.084742\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        0,\n" +
                "                                        105,\n" +
                "                                        165,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 3,\n" +
                "                                    \"location\": [\n" +
                "                                        14.448818,\n" +
                "                                        50.084596\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        105,\n" +
                "                                        195,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.450119,\n" +
                "                                        50.08443\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        30,\n" +
                "                                        105,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.450692,\n" +
                "                                        50.084358\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        105,\n" +
                "                                        195,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.451155,\n" +
                "                                        50.0843\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 100,\n" +
                "                                \"bearing_before\": 0,\n" +
                "                                \"location\": [\n" +
                "                                    14.446652,\n" +
                "                                    50.084837\n" +
                "                                ],\n" +
                "                                \"type\": \"depart\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Seifertova\",\n" +
                "                            \"weight\": 74.3\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 0,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 0,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.453412,\n" +
                "                                        50.08401\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.453412,\n" +
                "                                        50.08401\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        292\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.453412,\n" +
                "                                        50.08401\n" +
                "                                    ]\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 0,\n" +
                "                                \"bearing_before\": 112,\n" +
                "                                \"location\": [\n" +
                "                                    14.453412,\n" +
                "                                    50.08401\n" +
                "                                ],\n" +
                "                                \"type\": \"arrive\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Seifertova\",\n" +
                "                            \"weight\": 0\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"summary\": \"Seifertova\",\n" +
                "                    \"weight\": 74.3\n" +
                "                },\n" +
                "                {\n" +
                "                    \"annotation\": {\n" +
                "                        \"distance\": [\n" +
                "                            5.5814353774855,\n" +
                "                            11.862258901333774,\n" +
                "                            72.5165004907421,\n" +
                "                            14.126225903927095,\n" +
                "                            78.80460381062021,\n" +
                "                            8.177609255384677,\n" +
                "                            3.6047201252368506\n" +
                "                        ],\n" +
                "                        \"duration\": [\n" +
                "                            1,\n" +
                "                            1.7,\n" +
                "                            10.4,\n" +
                "                            2,\n" +
                "                            11.3,\n" +
                "                            1.2,\n" +
                "                            0.6\n" +
                "                        ],\n" +
                "                        \"speed\": [\n" +
                "                            5.6,\n" +
                "                            7,\n" +
                "                            7,\n" +
                "                            7.1,\n" +
                "                            7,\n" +
                "                            6.8,\n" +
                "                            6\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"distance\": 194.7,\n" +
                "                    \"duration\": 28.2,\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"distance\": 5.6,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 1,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.453412,\n" +
                "                                        50.08401\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.453485,\n" +
                "                                        50.083992\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        111\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.453412,\n" +
                "                                        50.08401\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 111,\n" +
                "                                \"bearing_before\": 0,\n" +
                "                                \"location\": [\n" +
                "                                    14.453412,\n" +
                "                                    50.08401\n" +
                "                                ],\n" +
                "                                \"type\": \"depart\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Seifertova\",\n" +
                "                            \"weight\": 1.6\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 189.1,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 27.2,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.453485,\n" +
                "                                        50.083992\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.4536,\n" +
                "                                        50.083915\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.454217,\n" +
                "                                        50.083397\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.454337,\n" +
                "                                        50.083296\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.454997,\n" +
                "                                        50.082728\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.455078,\n" +
                "                                        50.082676\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.455121,\n" +
                "                                        50.082659\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        135,\n" +
                "                                        225,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.453485,\n" +
                "                                        50.083992\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 140,\n" +
                "                                \"bearing_before\": 102,\n" +
                "                                \"location\": [\n" +
                "                                    14.453485,\n" +
                "                                    50.083992\n" +
                "                                ],\n" +
                "                                \"modifier\": \"slight right\",\n" +
                "                                \"type\": \"new name\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Táboritská\",\n" +
                "                            \"weight\": 27.2\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 0,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 0,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.455121,\n" +
                "                                        50.082659\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.455121,\n" +
                "                                        50.082659\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        302\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.455121,\n" +
                "                                        50.082659\n" +
                "                                    ]\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 0,\n" +
                "                                \"bearing_before\": 122,\n" +
                "                                \"location\": [\n" +
                "                                    14.455121,\n" +
                "                                    50.082659\n" +
                "                                ],\n" +
                "                                \"type\": \"arrive\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Táboritská\",\n" +
                "                            \"weight\": 0\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"summary\": \"Seifertova, Táboritská\",\n" +
                "                    \"weight\": 28.8\n" +
                "                }\n" +
                "            ],\n" +
                "            \"weight\": 103.1,\n" +
                "            \"weight_name\": \"routability\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"waypoints\": [\n" +
                "        {\n" +
                "            \"distance\": 0,\n" +
                "            \"hint\": \"84z2iQuN9okOAAAABwAAACoAAAAAAAAACh8JQcAZnEAiYM9BAAAAAA4AAAAHAAAAKgAAAAAAAADHpQAAPHDcAOU7_AI8cNwA5Tv8AgEAjxHQbqaE\",\n" +
                "            \"location\": [\n" +
                "                14.446652,\n" +
                "                50.084837\n" +
                "            ],\n" +
                "            \"name\": \"Seifertova\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"distance\": 0,\n" +
                "            \"hint\": \"_4z2iQON9okIAAAACwAAAAAAAAAtAQAASxSzQOqtrEAAAAAAqnUfQwgAAAALAAAAAAAAAC0BAADHpQAApIrcAKo4_AKkitwAqjj8AgAAPxLQbqaE\",\n" +
                "            \"location\": [\n" +
                "                14.453412,\n" +
                "                50.08401\n" +
                "            ],\n" +
                "            \"name\": \"Seifertova\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"distance\": 0,\n" +
                "            \"hint\": \"nLjqgACN9okFAAAABgAAABQAAAAKAQAAE-04QCs1Z0ATMzFB-rM5QwUAAAAGAAAAFAAAAAoBAADHpQAAUZHcAGMz_AJRkdwAYzP8AgEA7xLQbqaE\",\n" +
                "            \"location\": [\n" +
                "                14.455121,\n" +
                "                50.082659\n" +
                "            ],\n" +
                "            \"name\": \"Táboritská\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "\n";
    }













    public String findWinnerAndDelays(FastestCarRequest fastestCarRequest) {
        TimeTable fromSourceToWaypoints = getTimeTable(fastestCarRequest, fastestCarRequest.getOrigin(), "?sources=0");
        TimeTable fromWaypointsToDest = getTimeTable(fastestCarRequest, fastestCarRequest.getDestination(), "?destinations=0");
        ArrayList<Double> totalDurations = calculateTotalDurations(fromSourceToWaypoints, fromWaypointsToDest);
        totalDurations.forEach(
                d -> System.out.println(d)
        );
        return "s";
    }

    private TimeTable getTimeTable(FastestCarRequest fastestCarRequest, Location location, String s) {
        String uri = formUri(fastestCarRequest, location, s);
        return restTemplate.getForObject(uri, TimeTable.class);
    }

    private String formUri(FastestCarRequest fastestCarRequest, Location location, String s) {
        String uri = tableUri + location;
        for (int i = 0; i < fastestCarRequest.getWaypoints().size(); i++) {
            uri += ";" + fastestCarRequest.getWaypoints().get(i).toLocString();
        }
        uri += s;
        return uri;
    }

    private ArrayList<Double> calculateTotalDurations(TimeTable timeTable, TimeTable timeTable2) {
        ArrayList<Double> durations =  timeTable.getDurations().get(0);
        ArrayList<Double> finalDurations =  new ArrayList<>();
        for (int i = 0; i < durations.size(); i++) {
            finalDurations.add(i, durations.get(i) + timeTable2.getDurations().get(i).get(0));
        }
        return finalDurations;
    }
}
