package com.example.route_finder.entity;

import java.util.Map;

public class Response {
    private String winnerName;
    private Map<String, Double> delays;

    public Response(String winnerName, Map<String, Double> delays) {
        this.winnerName = winnerName;
        this.delays = delays;
    }
    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public Map<String, Double> getDelays() {
        return delays;
    }

    public void setDelays(Map<String, Double> delays) {
        this.delays = delays;
    }
}
