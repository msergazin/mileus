package com.example.route_finder.entity;

import com.example.route_finder.entity.osrm.objects.RouteResponse;
import com.example.route_finder.entity.osrm.objects.Waypoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class LocationTest {
    private Location origin;
    private Location dest;
    @BeforeEach
    public void setUp() throws JsonProcessingException {
        dest = new Location(50.082659, 14.455121);
        origin = new Location(50.084837, 14.446652);
    }

    @Test
    public void testHarvesine() {
        assert(Location.haversine(dest, origin) == 650.9898314766616);
    }
}
