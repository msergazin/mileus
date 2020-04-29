package com.example.route_finder.service.impl;

import com.example.route_finder.entity.*;
import com.example.route_finder.entity.osrm.objects.RouteResponse;
import com.example.route_finder.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {
    private final RestTemplate restTemplate;
    final String routeUri = "http://router.project-osrm.org/route/v1/driving/";

    @Autowired
    public RouteServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Response findTheWinnerAndCalculateDelays(FastestCarRequest fastestCarRequest) {
        Winner winner = findTheWinnerCar(fastestCarRequest);
        //remove the winner
        fastestCarRequest.getWaypoints().remove(winner.getWaypointName());
        HashMap<String, Double> delays = calculateDelays(fastestCarRequest, winner);
        return new Response(winner.getWaypointName().getName(), delays);
    }
    public HashMap<String, Double> calculateDelays(FastestCarRequest fastestCarRequest, Winner winner) {
        HashMap<String, Double> delays = new HashMap();
        delays.put(winner.getWaypointName().getName(), 0.0);

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
            String pointName = fastestCarRequest.getWaypoints().get(waypointOrCarIndex).getName();
            Double delay = calculateDelay(
                    winner.getTotalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit().get(pointName),
                    indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon, legDurations, uniqueIntersections);
            delays.put(pointName, delay);
        }

        return delays;
    }

    //TODO could also snap the location
    public Winner findTheWinnerCar(FastestCarRequest fastestCarRequest) {
        Double closestDistanceToDest = Double.MAX_VALUE;
        int indexOfaWinner = 99;
        HashMap<String, Double> totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit = new HashMap<>();
        for (int waypointOrCarIndex = 0; waypointOrCarIndex < fastestCarRequest.getWaypoints().size(); waypointOrCarIndex++) {
            RouteResponse routeResponse = getRouteResponseFromOSRM(fastestCarRequest, waypointOrCarIndex);
            DistanceAndDuration distanceToDestAndDuration = findHowFarTheCarIsFromTheOriginByTimeLimitByAir(routeResponse, fastestCarRequest);
            System.out.println("WayPoint: " + fastestCarRequest.getWaypoints().get(waypointOrCarIndex));
            //find closest car at time limit
            if (distanceToDestAndDuration.getDistanceToOrigin() < closestDistanceToDest) {
                closestDistanceToDest = distanceToDestAndDuration.getDistanceToOrigin();
                indexOfaWinner = waypointOrCarIndex;
            }
            //save distance travelled by the car
            totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit.put(
                    fastestCarRequest.getWaypoints().get(waypointOrCarIndex).getName(),
                    routeResponse.getTheRoute().getDuration() - distanceToDestAndDuration.getDurationSoFar()
            );
        }
        System.out.println("closest car at the time limit is : " + fastestCarRequest.getWaypoints().get(indexOfaWinner));
        System.out.println("distance to dest at the time limit: " + closestDistanceToDest);
        return new Winner(fastestCarRequest.getWaypoints().get(indexOfaWinner), closestDistanceToDest, totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit);
    }

    //TODO cache http request or save it somewhere
    private RouteResponse getRouteResponseFromOSRM(FastestCarRequest fastestCarRequest, int waypointOrCarIndex) {
        String uri = routeUri + fastestCarRequest.getOrigin() + ";"
                + fastestCarRequest.getWaypoints().get(waypointOrCarIndex).toLocString() + ";"
                + fastestCarRequest.getDestination()
                + "?steps=true&annotations=speed,distance,duration&geometries=geojson";
        return restTemplate.getForObject(uri, RouteResponse.class);
    }

    private DistanceAndDuration findHowFarTheCarIsFromTheOriginByTimeLimitByAir(RouteResponse routeResponse, FastestCarRequest fastestCarRequest) {
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
        return new DistanceAndDuration(
                Location.haversine(fastestCarRequest.getDestination(), lastKnownLocationWithinTimeLimit),
                durationVar);
    }

    //unite all the geometry coordinates intersection into one array
    private ArrayList<Location> getUniqueIntersectionsFromGeometryCoordinatesOfThisLeg(RouteResponse routeResponse, int legIndexStop) {
        Set uniqueIntersections = new LinkedHashSet<Location>();
        for (int step = 0; step < routeResponse.getTheRoute().getLegs().get(legIndexStop).getSteps().size(); step++) {

            if (routeResponse.getTheRoute().getLegs().get(legIndexStop).getSteps().get(step).getDistance() != 0 )
            routeResponse.getTheRoute().getLegs().get(legIndexStop).getSteps().get(step).getGeometry().getCoordinates()
                    .forEach(
                            intersectionCoordinates -> {
                                uniqueIntersections.add(Location.arrayOfCoordinatesToLocation(intersectionCoordinates));
                            }
                    );
        }
        return new ArrayList<Location> (uniqueIntersections);
    }

    private Double calculateDelay(
            Double totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit,
            int indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon,
            ArrayList<Double> legDurations, ArrayList<Location> uniqueIntersections) {
        Double timeRequiredToReachSameDistanceFromDestAsTheWinnerCar = 0.0;
        //# of durations in leg = (# of unique intersections - 1)
        for (int i = indexOfIntersectionWhereTheCarIsAtTheSameDistanceAsTheWinnerCarWhenItWon; i < uniqueIntersections.size() - 1; i++) {
            timeRequiredToReachSameDistanceFromDestAsTheWinnerCar += legDurations.get(i);
        }
        return totalDurationOfRouteMinusDurationTravelledByTheCarWithinTimeLimit - timeRequiredToReachSameDistanceFromDestAsTheWinnerCar;
    }

    private boolean isWithin5Meters(Winner winner, Double distanceToDest) {
        return Math.abs(distanceToDest - winner.getDistanceToDestinationAtTimeLimit()) < 5;
    }
}
