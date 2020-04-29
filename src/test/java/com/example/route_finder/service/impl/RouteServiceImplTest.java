package com.example.route_finder.service.impl;

import com.example.route_finder.entity.FastestCarRequest;
import com.example.route_finder.entity.Location;
import com.example.route_finder.entity.Winner;
import com.example.route_finder.entity.osrm.objects.RouteResponse;
import com.example.route_finder.entity.osrm.objects.Waypoint;
import com.example.route_finder.service.RouteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;

@SpringBootTest
public class RouteServiceImplTest {
    @Autowired
    RouteService routeService;
    @Autowired
    RestTemplate restTemplate;

    private RouteResponse routeResponse;
    private FastestCarRequest fastestCarRequest;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        restTemplate = Mockito.mock(RestTemplate.class);
        String mockJson = getMockJson();

        System.out.println("setup");
        ObjectMapper objectMapper = new ObjectMapper();
        routeResponse = objectMapper.readValue(mockJson, RouteResponse.class);


        Location dest = new Location(50.082659, 14.455121);
        Location origin = new Location(50.084837, 14.446652);
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint("Point A",50.08401,14.453412));
        waypoints.add(new Waypoint("Point B",50.086927,14.450142));
        fastestCarRequest = new FastestCarRequest(origin, dest, 100, waypoints);
    }

    @Test
    public void testFindTheWinnerCar() {
        Mockito.when(restTemplate.getForObject(
                        "http://router.project-osrm.org/route/v1/driving/14.446652,50.084837;14.453412,50.08401;14.455121,50.082659?steps=true&annotations=speed,distance,duration&geometries=geojson", RouteResponse.class))
                .thenReturn(routeResponse);

        Winner winner = routeService.findTheWinnerCar(fastestCarRequest);
        assert(winner.getDistanceToDestinationAtTimeLimit() == 11.710965932972623);
        assert(winner.getWaypointName().getName().equals("Point A"));
        assert(winner.getTotalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit().size() == 2);
    }

    private String getMockJson() {
        return "{\n" +
                "    \"code\": \"Ok\",\n" +
                "    \"routes\": [\n" +
                "        {\n" +
                "            \"distance\": 2377.6,\n" +
                "            \"duration\": 384,\n" +
                "            \"geometry\": {\n" +
                "                \"coordinates\": [\n" +
                "                    [\n" +
                "                        14.446652,\n" +
                "                        50.084837\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.443824,\n" +
                "                        50.085124\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.440401,\n" +
                "                        50.085317\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.43904,\n" +
                "                        50.085954\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.438307,\n" +
                "                        50.086227\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.438301,\n" +
                "                        50.086404\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.439547,\n" +
                "                        50.087033\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.440606,\n" +
                "                        50.087256\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.441857,\n" +
                "                        50.087365\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.443066,\n" +
                "                        50.087315\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.445894,\n" +
                "                        50.086745\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.448954,\n" +
                "                        50.086721\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.450142,\n" +
                "                        50.086927\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.451213,\n" +
                "                        50.087127\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.452247,\n" +
                "                        50.086555\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.450692,\n" +
                "                        50.084358\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        14.453485,\n" +
                "                        50.083992\n" +
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
                "                            4.8637853920976495,\n" +
                "                            74.6342310153647,\n" +
                "                            10.993013882973441,\n" +
                "                            51.07622978513133,\n" +
                "                            23.045650400634827,\n" +
                "                            6.870615810390457,\n" +
                "                            32.877498171311444,\n" +
                "                            31.780226843518946,\n" +
                "                            17.513833448420232,\n" +
                "                            79.36648066758373,\n" +
                "                            3.4838644134450787,\n" +
                "                            3.7241262963468333,\n" +
                "                            26.393023887015094,\n" +
                "                            8.923770768565705,\n" +
                "                            31.80254904054687,\n" +
                "                            2.4290599567806055,\n" +
                "                            5.068348912561826,\n" +
                "                            5.8192016890366505,\n" +
                "                            30.265871392020877,\n" +
                "                            9.871507149946726,\n" +
                "                            58.154037033625066,\n" +
                "                            52.29002376736998,\n" +
                "                            59.521949265503864,\n" +
                "                            0.9650698694778054,\n" +
                "                            7.528657635882102,\n" +
                "                            4.385120134097817,\n" +
                "                            8.0008659084419,\n" +
                "                            19.984091885961835,\n" +
                "                            28.022325759159443,\n" +
                "                            53.33258327535652,\n" +
                "                            11.978863338492996,\n" +
                "                            79.5418594541217,\n" +
                "                            48.53678099014058,\n" +
                "                            41.63511280436872,\n" +
                "                            63.24846831112322,\n" +
                "                            21.069991975282534,\n" +
                "                            2.326613101616951,\n" +
                "                            16.277144409520847,\n" +
                "                            7.7127850309130705,\n" +
                "                            16.274981905268284,\n" +
                "                            81.79034792358142,\n" +
                "                            89.49690713347314,\n" +
                "                            172.86695739035162,\n" +
                "                            45.531543384631796,\n" +
                "                            37.468454521992584,\n" +
                "                            25.39303656721887,\n" +
                "                            24.981717853651258\n" +
                "                        ],\n" +
                "                        \"duration\": [\n" +
                "                            0.8,\n" +
                "                            10,\n" +
                "                            2,\n" +
                "                            9.2,\n" +
                "                            4.1,\n" +
                "                            1.2,\n" +
                "                            4.7,\n" +
                "                            4.6,\n" +
                "                            2.3,\n" +
                "                            10.6,\n" +
                "                            0.5,\n" +
                "                            0.5,\n" +
                "                            3.7,\n" +
                "                            1.2,\n" +
                "                            7.2,\n" +
                "                            0.5,\n" +
                "                            1.1,\n" +
                "                            1.3,\n" +
                "                            4.4,\n" +
                "                            1.4,\n" +
                "                            8.4,\n" +
                "                            7.5,\n" +
                "                            14.3,\n" +
                "                            0.2,\n" +
                "                            1.6,\n" +
                "                            0.9,\n" +
                "                            1.7,\n" +
                "                            2,\n" +
                "                            2.8,\n" +
                "                            5.3,\n" +
                "                            1.2,\n" +
                "                            12.5,\n" +
                "                            7.6,\n" +
                "                            6.5,\n" +
                "                            9.9,\n" +
                "                            3.3,\n" +
                "                            0.4,\n" +
                "                            2.3,\n" +
                "                            1.1,\n" +
                "                            2.3,\n" +
                "                            10.5,\n" +
                "                            11.5,\n" +
                "                            23.9,\n" +
                "                            5.9,\n" +
                "                            4.8,\n" +
                "                            3.3,\n" +
                "                            3.7\n" +
                "                        ],\n" +
                "                        \"speed\": [\n" +
                "                            6.1,\n" +
                "                            7.5,\n" +
                "                            5.5,\n" +
                "                            5.6,\n" +
                "                            5.6,\n" +
                "                            5.7,\n" +
                "                            7,\n" +
                "                            6.9,\n" +
                "                            7.6,\n" +
                "                            7.5,\n" +
                "                            7,\n" +
                "                            7.4,\n" +
                "                            7.1,\n" +
                "                            7.4,\n" +
                "                            4.4,\n" +
                "                            4.9,\n" +
                "                            4.6,\n" +
                "                            4.5,\n" +
                "                            6.9,\n" +
                "                            7.1,\n" +
                "                            6.9,\n" +
                "                            7,\n" +
                "                            4.2,\n" +
                "                            4.8,\n" +
                "                            4.7,\n" +
                "                            4.9,\n" +
                "                            4.7,\n" +
                "                            10,\n" +
                "                            10,\n" +
                "                            10.1,\n" +
                "                            10,\n" +
                "                            6.4,\n" +
                "                            6.4,\n" +
                "                            6.4,\n" +
                "                            6.4,\n" +
                "                            6.4,\n" +
                "                            5.8,\n" +
                "                            7.1,\n" +
                "                            7,\n" +
                "                            7.1,\n" +
                "                            7.8,\n" +
                "                            7.8,\n" +
                "                            7.2,\n" +
                "                            7.7,\n" +
                "                            7.8,\n" +
                "                            7.7,\n" +
                "                            6.8\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"distance\": 1539.1,\n" +
                "                    \"duration\": 233.1,\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"distance\": 631.7,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 106,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.446652,\n" +
                "                                        50.084837\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.446585,\n" +
                "                                        50.084845\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.44555,\n" +
                "                                        50.084941\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.445398,\n" +
                "                                        50.084957\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.444693,\n" +
                "                                        50.085036\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.444375,\n" +
                "                                        50.085072\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.44428,\n" +
                "                                        50.085082\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443824,\n" +
                "                                        50.085124\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443381,\n" +
                "                                        50.085153\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443136,\n" +
                "                                        50.085162\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.442027,\n" +
                "                                        50.085215\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441986,\n" +
                "                                        50.085232\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441942,\n" +
                "                                        50.08525\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441576,\n" +
                "                                        50.085284\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441451,\n" +
                "                                        50.085286\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441006,\n" +
                "                                        50.085271\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.440972,\n" +
                "                                        50.08527\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.440901,\n" +
                "                                        50.085269\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.44082,\n" +
                "                                        50.085275\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.440401,\n" +
                "                                        50.085317\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.44028,\n" +
                "                                        50.08536\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.439632,\n" +
                "                                        50.085677\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.43904,\n" +
                "                                        50.085954\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438319,\n" +
                "                                        50.086223\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438307,\n" +
                "                                        50.086227\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        281\n" +
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
                "                                        0,\n" +
                "                                        105,\n" +
                "                                        165,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 1,\n" +
                "                                    \"location\": [\n" +
                "                                        14.446585,\n" +
                "                                        50.084845\n" +
                "                                    ],\n" +
                "                                    \"out\": 3\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        0,\n" +
                "                                        105,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 1,\n" +
                "                                    \"location\": [\n" +
                "                                        14.44428,\n" +
                "                                        50.085082\n" +
                "                                    ],\n" +
                "                                    \"out\": 2\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        90,\n" +
                "                                        180,\n" +
                "                                        270\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.443381,\n" +
                "                                        50.085153\n" +
                "                                    ],\n" +
                "                                    \"out\": 2\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        90,\n" +
                "                                        180,\n" +
                "                                        255\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.442027,\n" +
                "                                        50.085215\n" +
                "                                    ],\n" +
                "                                    \"out\": 2\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        90,\n" +
                "                                        270,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"lanes\": [\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"straight\",\n" +
                "                                                \"left\",\n" +
                "                                                \"slight right\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": true\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.441451,\n" +
                "                                        50.085286\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        90,\n" +
                "                                        180,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"lanes\": [\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"straight\",\n" +
                "                                                \"left\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": true\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.44082,\n" +
                "                                        50.085275\n" +
                "                                    ],\n" +
                "                                    \"out\": 2\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        120,\n" +
                "                                        300\n" +
                "                                    ],\n" +
                "                                    \"classes\": [\n" +
                "                                        \"tunnel\"\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.43904,\n" +
                "                                        50.085954\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 281,\n" +
                "                                \"bearing_before\": 0,\n" +
                "                                \"location\": [\n" +
                "                                    14.446652,\n" +
                "                                    50.084837\n" +
                "                                ],\n" +
                "                                \"type\": \"depart\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Seifertova\",\n" +
                "                            \"weight\": 132.8\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 907.4,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 127.10000000000001,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.438307,\n" +
                "                                        50.086227\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438292,\n" +
                "                                        50.086294\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438283,\n" +
                "                                        50.086333\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438301,\n" +
                "                                        50.086404\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438508,\n" +
                "                                        50.086525\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.438802,\n" +
                "                                        50.086692\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.439412,\n" +
                "                                        50.086969\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.439547,\n" +
                "                                        50.087033\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.440606,\n" +
                "                                        50.087256\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441276,\n" +
                "                                        50.087331\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.441857,\n" +
                "                                        50.087365\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.442743,\n" +
                "                                        50.087351\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443034,\n" +
                "                                        50.087319\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443066,\n" +
                "                                        50.087315\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443282,\n" +
                "                                        50.087268\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443385,\n" +
                "                                        50.087247\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.443602,\n" +
                "                                        50.087202\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.444698,\n" +
                "                                        50.086987\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.445894,\n" +
                "                                        50.086745\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.448316,\n" +
                "                                        50.086722\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.448954,\n" +
                "                                        50.086721\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.449464,\n" +
                "                                        50.086801\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.449806,\n" +
                "                                        50.086864\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450142,\n" +
                "                                        50.086927\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        0,\n" +
                "                                        120,\n" +
                "                                        315\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 1,\n" +
                "                                    \"location\": [\n" +
                "                                        14.438307,\n" +
                "                                        50.086227\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        45,\n" +
                "                                        180,\n" +
                "                                        255\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 1,\n" +
                "                                    \"lanes\": [\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"straight\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": true\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"slight right\",\n" +
                "                                                \"right\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": false\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.438301,\n" +
                "                                        50.086404\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        0,\n" +
                "                                        105,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"lanes\": [\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"left\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": false\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"straight\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": true\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.443282,\n" +
                "                                        50.087268\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        30,\n" +
                "                                        105,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"lanes\": [\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"left\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": false\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"indications\": [\n" +
                "                                                \"straight\"\n" +
                "                                            ],\n" +
                "                                            \"valid\": true\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.443602,\n" +
                "                                        50.087202\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        90,\n" +
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
                "                                        14.445894,\n" +
                "                                        50.086745\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        0,\n" +
                "                                        90,\n" +
                "                                        180,\n" +
                "                                        270\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 3,\n" +
                "                                    \"location\": [\n" +
                "                                        14.448316,\n" +
                "                                        50.086722\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        75,\n" +
                "                                        255,\n" +
                "                                        345\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 1,\n" +
                "                                    \"location\": [\n" +
                "                                        14.449806,\n" +
                "                                        50.086864\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 358,\n" +
                "                                \"bearing_before\": 299,\n" +
                "                                \"location\": [\n" +
                "                                    14.438307,\n" +
                "                                    50.086227\n" +
                "                                ],\n" +
                "                                \"modifier\": \"right\",\n" +
                "                                \"type\": \"turn\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Husitská\",\n" +
                "                            \"weight\": 145.29999999999998\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 0,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 0,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.450142,\n" +
                "                                        50.086927\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450142,\n" +
                "                                        50.086927\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        254\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.450142,\n" +
                "                                        50.086927\n" +
                "                                    ]\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 0,\n" +
                "                                \"bearing_before\": 74,\n" +
                "                                \"location\": [\n" +
                "                                    14.450142,\n" +
                "                                    50.086927\n" +
                "                                ],\n" +
                "                                \"type\": \"arrive\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Husitská\",\n" +
                "                            \"weight\": 0\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"summary\": \"Seifertova, Husitská\",\n" +
                "                    \"weight\": 278.1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"annotation\": {\n" +
                "                        \"distance\": [\n" +
                "                            68.51760842344407,\n" +
                "                            11.088529809584895,\n" +
                "                            10.134365462764647,\n" +
                "                            7.241976547589445,\n" +
                "                            41.68732732903312,\n" +
                "                            14.4249551384415,\n" +
                "                            5.352615251515137,\n" +
                "                            18.929779775689553,\n" +
                "                            8.695452474237257,\n" +
                "                            163.45316640202614,\n" +
                "                            89.24160084789486,\n" +
                "                            5.286225716084526,\n" +
                "                            1.7129305982947973,\n" +
                "                            12.846507611173664,\n" +
                "                            20.822390000599597,\n" +
                "                            13.638765287872276,\n" +
                "                            2.6075372289539223,\n" +
                "                            3.9318117425590127,\n" +
                "                            80.79173288923869,\n" +
                "                            58.02392734333691,\n" +
                "                            10.963276870219408,\n" +
                "                            11.862258901333774,\n" +
                "                            72.5165004907421,\n" +
                "                            14.126225903927095,\n" +
                "                            78.80460381062021,\n" +
                "                            8.177609255384677,\n" +
                "                            3.6047201252368506\n" +
                "                        ],\n" +
                "                        \"duration\": [\n" +
                "                            9.8,\n" +
                "                            1.6,\n" +
                "                            1.5,\n" +
                "                            1,\n" +
                "                            6,\n" +
                "                            2,\n" +
                "                            0.7,\n" +
                "                            2.4,\n" +
                "                            1.6,\n" +
                "                            29.4,\n" +
                "                            17.8,\n" +
                "                            1.1,\n" +
                "                            0.3,\n" +
                "                            2.4,\n" +
                "                            3.9,\n" +
                "                            2.6,\n" +
                "                            0.5,\n" +
                "                            0.7,\n" +
                "                            15.3,\n" +
                "                            11,\n" +
                "                            2.1,\n" +
                "                            1.7,\n" +
                "                            10.4,\n" +
                "                            2,\n" +
                "                            11.3,\n" +
                "                            1.2,\n" +
                "                            0.6\n" +
                "                        ],\n" +
                "                        \"speed\": [\n" +
                "                            7,\n" +
                "                            6.9,\n" +
                "                            6.8,\n" +
                "                            7.2,\n" +
                "                            6.9,\n" +
                "                            7.2,\n" +
                "                            7.6,\n" +
                "                            7.9,\n" +
                "                            5.4,\n" +
                "                            5.6,\n" +
                "                            5,\n" +
                "                            4.8,\n" +
                "                            5.7,\n" +
                "                            5.4,\n" +
                "                            5.3,\n" +
                "                            5.2,\n" +
                "                            5.2,\n" +
                "                            5.6,\n" +
                "                            5.3,\n" +
                "                            5.3,\n" +
                "                            5.2,\n" +
                "                            7,\n" +
                "                            7,\n" +
                "                            7.1,\n" +
                "                            7,\n" +
                "                            6.8,\n" +
                "                            6\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"distance\": 838.5,\n" +
                "                    \"duration\": 150.9,\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"distance\": 79.6,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 14,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.450142,\n" +
                "                                        50.086927\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451063,\n" +
                "                                        50.087101\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451213,\n" +
                "                                        50.087127\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        74\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"location\": [\n" +
                "                                        14.450142,\n" +
                "                                        50.086927\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 74,\n" +
                "                                \"bearing_before\": 0,\n" +
                "                                \"location\": [\n" +
                "                                    14.450142,\n" +
                "                                    50.086927\n" +
                "                                ],\n" +
                "                                \"type\": \"depart\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Husitská\",\n" +
                "                            \"weight\": 29.8\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 97.8,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 14.5,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.451213,\n" +
                "                                        50.087127\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451302,\n" +
                "                                        50.087056\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451367,\n" +
                "                                        50.087006\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451827,\n" +
                "                                        50.086775\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451981,\n" +
                "                                        50.086691\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.452042,\n" +
                "                                        50.086663\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.452247,\n" +
                "                                        50.086555\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        75,\n" +
                "                                        150,\n" +
                "                                        255,\n" +
                "                                        330\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.451213,\n" +
                "                                        50.087127\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        135,\n" +
                "                                        225,\n" +
                "                                        315\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        false,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.451827,\n" +
                "                                        50.086775\n" +
                "                                    ],\n" +
                "                                    \"out\": 0\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        75,\n" +
                "                                        135,\n" +
                "                                        315\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.452042,\n" +
                "                                        50.086663\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 142,\n" +
                "                                \"bearing_before\": 73,\n" +
                "                                \"location\": [\n" +
                "                                    14.451213,\n" +
                "                                    50.087127\n" +
                "                                ],\n" +
                "                                \"modifier\": \"right\",\n" +
                "                                \"type\": \"turn\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Prokopova\",\n" +
                "                            \"weight\": 20.2\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 268.4,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 56.7,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
                "                                    [\n" +
                "                                        14.452247,\n" +
                "                                        50.086555\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.452196,\n" +
                "                                        50.086484\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.451242,\n" +
                "                                        50.085148\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.45073,\n" +
                "                                        50.084416\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450702,\n" +
                "                                        50.084372\n" +
                "                                    ],\n" +
                "                                    [\n" +
                "                                        14.450692,\n" +
                "                                        50.084358\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        135,\n" +
                "                                        210,\n" +
                "                                        315\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        true,\n" +
                "                                        true,\n" +
                "                                        false\n" +
                "                                    ],\n" +
                "                                    \"in\": 2,\n" +
                "                                    \"location\": [\n" +
                "                                        14.452247,\n" +
                "                                        50.086555\n" +
                "                                    ],\n" +
                "                                    \"out\": 1\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        30,\n" +
                "                                        90,\n" +
                "                                        210,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
                "                                    \"location\": [\n" +
                "                                        14.451242,\n" +
                "                                        50.085148\n" +
                "                                    ],\n" +
                "                                    \"out\": 2\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"maneuver\": {\n" +
                "                                \"bearing_after\": 203,\n" +
                "                                \"bearing_before\": 129,\n" +
                "                                \"location\": [\n" +
                "                                    14.452247,\n" +
                "                                    50.086555\n" +
                "                                ],\n" +
                "                                \"modifier\": \"right\",\n" +
                "                                \"type\": \"turn\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Milíčova\",\n" +
                "                            \"weight\": 96.19999999999999\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"distance\": 203.6,\n" +
                "                            \"driving_side\": \"right\",\n" +
                "                            \"duration\": 38.5,\n" +
                "                            \"geometry\": {\n" +
                "                                \"coordinates\": [\n" +
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
                "                                        14.453485,\n" +
                "                                        50.083992\n" +
                "                                    ]\n" +
                "                                ],\n" +
                "                                \"type\": \"LineString\"\n" +
                "                            },\n" +
                "                            \"intersections\": [\n" +
                "                                {\n" +
                "                                    \"bearings\": [\n" +
                "                                        30,\n" +
                "                                        105,\n" +
                "                                        285\n" +
                "                                    ],\n" +
                "                                    \"entry\": [\n" +
                "                                        false,\n" +
                "                                        true,\n" +
                "                                        true\n" +
                "                                    ],\n" +
                "                                    \"in\": 0,\n" +
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
                "                                \"bearing_after\": 99,\n" +
                "                                \"bearing_before\": 203,\n" +
                "                                \"location\": [\n" +
                "                                    14.450692,\n" +
                "                                    50.084358\n" +
                "                                ],\n" +
                "                                \"modifier\": \"left\",\n" +
                "                                \"type\": \"end of road\"\n" +
                "                            },\n" +
                "                            \"mode\": \"driving\",\n" +
                "                            \"name\": \"Seifertova\",\n" +
                "                            \"weight\": 39.199999999999996\n" +
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
                "                    \"summary\": \"Milíčova, Seifertova\",\n" +
                "                    \"weight\": 212.6\n" +
                "                }\n" +
                "            ],\n" +
                "            \"weight\": 490.70000000000005,\n" +
                "            \"weight_name\": \"routability\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"waypoints\": [\n" +
                "        {\n" +
                "            \"distance\": 0,\n" +
                "            \"hint\": \"2TOeivEznooOAAAABwAAACoAAAAAAAAACh8JQcAZnEAiYM9BAAAAAA4AAAAHAAAAKgAAAAAAAADwpQAAPHDcAOU7_AI8cNwA5Tv8AgEAjxFwGeRW\",\n" +
                "            \"location\": [\n" +
                "                14.446652,\n" +
                "                50.084837\n" +
                "            ],\n" +
                "            \"name\": \"Seifertova\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"distance\": 0,\n" +
                "            \"hint\": \"ZTieiv3l-IBbAAAAJQAAAA8AAAAAAAAAKG2JQqhsyEH97TFBAAAAAFsAAAAlAAAADwAAAAAAAADwpQAA3n3cAA9E_ALefdwAD0T8AgEA3w9wGeRW\",\n" +
                "            \"location\": [\n" +
                "                14.450142,\n" +
                "                50.086927\n" +
                "            ],\n" +
                "            \"name\": \"Husitská\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"distance\": 0,\n" +
                "            \"hint\": \"geX4gOYznooFAAAABgAAABQAAAAKAQAAE-04QCs1Z0ATMzFB-rM5QwUAAAAGAAAAFAAAAAoBAADwpQAAUZHcAGMz_AJRkdwAYzP8AgEA7xJwGeRW\",\n" +
                "            \"location\": [\n" +
                "                14.455121,\n" +
                "                50.082659\n" +
                "            ],\n" +
                "            \"name\": \"Táboritská\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

//    @Test
//    public void test
}
